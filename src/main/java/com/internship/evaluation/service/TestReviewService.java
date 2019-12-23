package com.internship.evaluation.service;

import com.internship.evaluation.config.TestDbConfiguration;
import com.internship.evaluation.model.dto.generate_test.SingleChoiceCandidateAnswerDTO;
import com.internship.evaluation.model.dto.generate_test.SingleChoiceTaskAnswerDTO;
import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import com.internship.evaluation.model.dto.test.CandidateTestResultsDTO;
import com.internship.evaluation.model.dto.test.SqlCandidateResultDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.CandidateSingleTask;
import com.internship.evaluation.model.entity.CandidateSqlTask;
import com.internship.evaluation.model.test_db_entities.Employee;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestReviewService {

    @Autowired
    private Environment environment;

    @Autowired
    private TestDbConfiguration testDbConfiguration;

    private final CandidateService candidateService;
    private final TestTokenService testTokenService;

    public CandidateTestResultsDTO reviewCandidateTest(String token) {
        Candidate candidate = new Candidate();
        candidate = testTokenService.getCandidateByToken(token);


        CandidateTestResultsDTO candidateResults = new CandidateTestResultsDTO();
        candidateResults.setToken(token);
        candidateResults.setCandidateId(testTokenService.getCandidateByToken(token).getId());
        SingleChoiceCandidateAnswerDTO singleChoiceResults = getSingleChoiceTasksResults(token, candidate);
        SqlCandidateResultDTO sqlResults = getSqlResults(token, candidate);

        candidateResults.setSingleChoiceTasksResults(singleChoiceResults);
        candidateResults.setSqlResults(sqlResults);
        return candidateResults;
    }

    private SingleChoiceCandidateAnswerDTO getSingleChoiceTasksResults(String token, Candidate candidate){
        SingleChoiceCandidateAnswerDTO singleChoiceCandidateAnswerDTO = new SingleChoiceCandidateAnswerDTO();
        ArrayList<SingleChoiceTaskAnswerDTO> singleChoiceAnswers = new ArrayList<>();
        if (candidate != null){
            for (CandidateSingleTask candidateSingleTask : candidate.getCandidateSingleTasks()){
                SingleChoiceTaskAnswerDTO answerDTO = new SingleChoiceTaskAnswerDTO();
                answerDTO.setSingleChoiceTaskId(candidateSingleTask.getId());
                answerDTO.setSelectedAnswerOptionId(candidateSingleTask.getAnswersOption().getId());
                answerDTO.setSelectedAnswerOptionText(candidateSingleTask.getAnswersOption().getAnswerOptionValue());
                answerDTO.setIsCorrect(candidateSingleTask.getAnswersOption().isCorrect());
                singleChoiceAnswers.add(answerDTO);
            }
        }
        singleChoiceCandidateAnswerDTO.setToken(token);
        singleChoiceCandidateAnswerDTO.setAnswers(singleChoiceAnswers);
        return singleChoiceCandidateAnswerDTO;
    }

    private SqlCandidateResultDTO getSqlResults(String token, Candidate candidate) {
        ArrayList<SqlAnswersDTO> sqlAnswersBeforeReview = new ArrayList<>();
        ArrayList<SqlAnswersDTO> sqlAnswersAfterReview = new ArrayList<>();
        SqlCandidateResultDTO sqlResult = new SqlCandidateResultDTO();
        if (candidate != null) {
            for (CandidateSqlTask sqlTask : candidate.getCandidateSqlTasks()) {
                SqlAnswersDTO sqlAnswer = new SqlAnswersDTO(sqlTask);
                sqlAnswer.setCorrectStatement(sqlTask.getSqlTask().getCorrectStatement());
                sqlAnswersBeforeReview.add(sqlAnswer);
            }
        }

        DataSource db = testDbConfiguration.testDataSource();
        Connection con = null;
        try {
            con = db.getConnection();
            try {
                for (SqlAnswersDTO answer : sqlAnswersBeforeReview) {

                    Statement statement = con.createStatement(
                            ResultSet.CONCUR_UPDATABLE,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.HOLD_CURSORS_OVER_COMMIT);

                    int numberColumnsCorrectStatement = statement.executeQuery(answer.getCorrectStatement()).getMetaData().getColumnCount();
                    int numberColumnsCandidateStatement = 0;

                    //try-check to catch errors with candidate's answer
                    try {
                        numberColumnsCandidateStatement = statement.executeQuery(answer.getSqlAnswer()).getMetaData().getColumnCount();

                        //CHECK #1 - for restricted words
                        if (!containsRestrictedWords(answer.getSqlAnswer(), token)) {

                            //CHECK #2 - for number of columns
                            if (numberColumnsCandidateStatement == numberColumnsCorrectStatement) {

                                //CHECK #3 - for number of rows
                                if (!nrRowsIsNotEqual(answer.getCorrectStatement(), answer.getSqlAnswer())) {

                                    //CHECK #4 - for main content
                                    if (!isContentDifferent(answer.getCorrectStatement(), answer.getSqlAnswer())) {
                                        sqlAnswersAfterReview.add(getAnswerAfterReview(answer, true));
                                    } else {
                                        sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                                    }
                                } else {
                                    sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                                }
                            } else {
                                sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                            }
                        } else {
                            sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                        }
                    } catch (SQLException e) {
                        log.info("Error while running candidate's sql answer. Token: {}, Answer: {}", token, answer.getSqlAnswer());
                        sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                    }
                    sqlResult.setCheckedCandidateSqlAnswers(sqlAnswersAfterReview);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error("Error while closing connection to the TEST DB: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            log.error("Error while getting connection to the TEST DB: " + e.getMessage());
        }
        return sqlResult;
    }

    //region PRIVATE METHODS TO CHECK CANDIDATE SQL ANSWERS

    private SqlAnswersDTO getAnswerAfterReview(SqlAnswersDTO answer, boolean result) {
        SqlAnswersDTO answerProcessed = answer;
        answer.setIsCorrect(result);
        return answerProcessed;
    }

    //check for restricted words
    private boolean containsRestrictedWords(String candidateAnswer, String token) {
        String answerModified = candidateAnswer.toLowerCase();
        if (candidateAnswer.contains("delete")
                || candidateAnswer.contains("drop")
                || candidateAnswer.contains("insert")
                || candidateAnswer.contains("update")) {
            log.warn("Candidate with token {} provided sql statement with restricted workds. Statemens: {}", token, candidateAnswer);
            return true;
        } else {
            return false;
        }
    }

    //compare number of rows
    private boolean nrRowsIsNotEqual(String correctStt, String candidateStt) {
        DataSource db = testDbConfiguration.testDataSource();
        Connection con = null;
        ResultSet correctCopy;
        ResultSet candidateCopy;
        int rowsInCorrectStmt = 0;
        int rowsInCandidateStmt = 0;

        //correct statement processing
        try {
            con = db.getConnection();
            Statement statem = con.createStatement(
                    ResultSet.CONCUR_UPDATABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            correctCopy = statem.executeQuery(correctStt);
            try {
                while (correctCopy.next()) {
                    ++rowsInCorrectStmt;
                }
            } catch (SQLException e) {
                log.error("Error while counting rows of set: " + correctCopy.toString());
            }
        } catch (SQLException e) {
            log.error("Error while getting DB connection");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Error while closing TEST_SQL DB connection");
            }
        }

        //correct statement processing
        try {
            con = db.getConnection();
            Statement statem = con.createStatement(
                    ResultSet.CONCUR_UPDATABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            candidateCopy = statem.executeQuery(candidateStt);
            try {
                while (candidateCopy.next()) {
                    ++rowsInCandidateStmt;
                }
            } catch (SQLException e) {
                log.error("Error while counting rows of set: " + candidateCopy.toString());
            }
        } catch (SQLException e) {
            log.error("Error while getting DB connection");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Error while closing TEST_SQL DB connection");
            }
        }

        if (rowsInCandidateStmt != rowsInCorrectStmt) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks basic content for equality in case Nr of rows and Nr of columns is equal.
     * Order of columns should be the same.
     *
     * @param correctStt
     * @param candidateStt
     * @return
     */
    private boolean isContentDifferent(String correctStt, String candidateStt) {
        boolean result = false;
        ResultSet correctResult = null;
        ResultSet candidateResult = null;

        DataSource db = testDbConfiguration.testDataSource();
        Connection con = null;

        //correct statement processing
        try {
            con = db.getConnection();
            Statement statem = con.createStatement(
                    ResultSet.CONCUR_UPDATABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            correctResult = statem.executeQuery(correctStt);
        } catch (SQLException e) {
            log.error("Error while getting DB connection");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Error while closing TEST_SQL DB connection");
            }
        }

        //candidate statement processing
        try {
            con = db.getConnection();
            Statement statem = con.createStatement(
                    ResultSet.CONCUR_UPDATABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            candidateResult = statem.executeQuery(candidateStt);
        } catch (SQLException e) {
            log.error("Error while getting DB connection");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Error while closing TEST_SQL DB connection");
            }
        }

        //results comparison
        try {
            ResultSetMetaData mdCorrect = correctResult.getMetaData();
            int nrColumns = mdCorrect.getColumnCount();
            while (correctResult.next() && candidateResult.next()) {
                for (int i = 0; i < nrColumns; i++) {
                    Object objectFromCorrect = correctResult.getObject(i);
                    Object objectFromCandidate = candidateResult.getObject(i);
                    if (!objectFromCandidate.equals(objectFromCorrect)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error while reading SQL result sets during SQL statement check");
        }
        return result;
    }

    //endregion

}
