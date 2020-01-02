package com.internship.evaluation.service;

import com.internship.evaluation.config.TestDbConfiguration;
import com.internship.evaluation.model.dto.generate_test.*;
import com.internship.evaluation.model.dto.test.CandidateTestResultsDTO;
import com.internship.evaluation.model.dto.test.SqlCandidateResultDTO;
import com.internship.evaluation.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

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
        Candidate candidate;
        candidate = testTokenService.getCandidateByToken(token);

        CandidateTestResultsDTO candidateResults = new CandidateTestResultsDTO();
        candidateResults.setToken(token);
        candidateResults.setCandidateId(testTokenService.getCandidateByToken(token).getId());

        SingleChoiceCandidateAnswerDTO singleChoiceResults = getSingleChoiceTasksResults(candidate);
        MultiChoiceCandidateAnswerDTO multiChoiceResults = getMultiChoiceTasksResults(token, candidate);
        SqlCandidateResultDTO sqlResults = getSqlResults(token, candidate);

        candidateResults.setSingleChoiceTasksResults(singleChoiceResults);
        candidateResults.setMultiChoiceCandidateAnswerDTO(multiChoiceResults);
        candidateResults.setSqlResults(sqlResults);

        return candidateResults;
    }

    //region MULTI_CHOICE_TASKS processing
    private MultiChoiceCandidateAnswerDTO getMultiChoiceTasksResults(String token, Candidate candidate) {
        MultiChoiceCandidateAnswerDTO multiChoiceResult = new MultiChoiceCandidateAnswerDTO();
        ArrayList<MultiChoiceTaskAnswerDTO> listAnswerResults = new ArrayList<>();
        List<CandidateMultiTask> questionsAsked = testTokenService.getCandidateByToken(token).getCandidateMultiTasks();
        if (questionsAsked.size() > 0) {
            for (CandidateMultiTask taskAsked : questionsAsked) {
                MultiChoiceTaskAnswerDTO answer = new MultiChoiceTaskAnswerDTO();
                answer.setMultiChoiceTaskId(taskAsked.getId());
                Map<Long, String> correctAnswers = new HashMap<>();
                for (AnswersOption ao : taskAsked.getTask().getAnswersOptions()) {
                    correctAnswers.put(ao.getId(), ao.getAnswerOptionValue());
                }
                Map<Long, String> selectedAnswers = getCandidateMultiTasksAnswers(candidate, taskAsked.getId());
                answer.setAoCorrectAnswers(correctAnswers);
                answer.setAoSelectedAnswers(selectedAnswers);
                answer.setCorrect(areMapsEqual(correctAnswers, selectedAnswers));
                listAnswerResults.add(answer);
            }
        }
        multiChoiceResult.setMultiChoiceAnswers(listAnswerResults);
        return multiChoiceResult;
    }

    private Map<Long, String> getCandidateMultiTasksAnswers(Candidate candidate, Long taskId) {
        Map<Long, String> answers = new HashMap<>();
        if (candidate != null) {
            Optional<CandidateMultiTask> mcTaskOptional = candidate.getCandidateMultiTasks()
                    .stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst();

            if (mcTaskOptional.isPresent()) {
                List<AnswersOption> taskAnswerOptions = mcTaskOptional.get().getAnswersOptions();
                for (AnswersOption ao : taskAnswerOptions) {
                    answers.put(ao.getId(), ao.getAnswerOptionValue());
                }
            }
        }
        return answers;
    }

    private boolean areMapsEqual(Map map1, Map map2) {
        if (map1.equals(map2)) {
            return true;
        } else {
            return false;
        }
    }

    //endregion

    private SingleChoiceCandidateAnswerDTO getSingleChoiceTasksResults(Candidate candidate) {
        SingleChoiceCandidateAnswerDTO singleChoiceCandidateAnswerDTO = new SingleChoiceCandidateAnswerDTO();
        ArrayList<SingleChoiceTaskAnswerDTO> singleChoiceAnswers = new ArrayList<>();
        if (candidate != null) {
            for (CandidateSingleTask candidateSingleTask : candidate.getCandidateSingleTasks()) {
                SingleChoiceTaskAnswerDTO answerDTO = new SingleChoiceTaskAnswerDTO();
                answerDTO.setSingleChoiceTaskId(candidateSingleTask.getId());
                if (candidateSingleTask.getAnswersOption() != null) {
                    answerDTO.setSelectedAnswerOptionId(candidateSingleTask.getAnswersOption().getId());
                    answerDTO.setSelectedAnswerOptionText(candidateSingleTask.getAnswersOption().getAnswerOptionValue());
                    answerDTO.setIsCorrect(candidateSingleTask.getAnswersOption().isCorrect());
                }
                singleChoiceAnswers.add(answerDTO);
            }
        }
        singleChoiceCandidateAnswerDTO.setAnswers(singleChoiceAnswers);
        return singleChoiceCandidateAnswerDTO;
    }

    private ArrayList<SqlAnswersDTO> initializeCandidateSqlAnswers(Candidate candidate) {
        ArrayList<SqlAnswersDTO> sqlAnswersBeforeReview = new ArrayList<>();
        if (candidate != null) {
            for (CandidateSqlTask sqlTask : candidate.getCandidateSqlTasks()) {
                SqlAnswersDTO sqlAnswer = new SqlAnswersDTO(sqlTask);
                sqlAnswer.setCorrectStatement(sqlTask.getSqlTask().getCorrectStatement());
                sqlAnswer.setAreRowsOrdered(sqlTask.getSqlTask().isRowsAreOrdered());
                sqlAnswer.setAreColumnsNamed(sqlTask.getSqlTask().isColumnsAreNamed());
                sqlAnswersBeforeReview.add(sqlAnswer);
            }
        }
        return sqlAnswersBeforeReview;
    }

    private SqlCandidateResultDTO getSqlResults(String token, Candidate candidate) {
        ArrayList<SqlAnswersDTO> sqlAnswersBeforeReview = initializeCandidateSqlAnswers(candidate);
        ArrayList<SqlAnswersDTO> sqlAnswersAfterReview = new ArrayList<>();
        SqlCandidateResultDTO sqlResult = new SqlCandidateResultDTO();

        DataSource db = testDbConfiguration.testDataSource();
        Connection con = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);

            for (SqlAnswersDTO answer : sqlAnswersBeforeReview) {
                sqlAnswersAfterReview = processSqlAnswer(answer, sqlAnswersAfterReview, con);
            }
            setSqlMessages(candidate, sqlAnswersAfterReview);
        } catch (SQLException e) {
            log.error("Error while connecting to DB: {}, token: {}", e.getMessage(), token);
        } catch (Exception e) {
            log.error("Error while analysing candidate's SQL answers: {}, token: {}", e.getMessage(), token);
        } finally {
            sqlResult.setCheckedCandidateSqlAnswers(sqlAnswersAfterReview);
            try {
                if (con != null) {
                    con.rollback();
                    con.close();
                }
            } catch (SQLException e) {
                log.error("Error while closing connection to the TEST DB: " + e.getMessage());
            }
        }
        return sqlResult;
    }


    //region PRIVATE METHODS TO CHECK CANDIDATE SQL ANSWERS

    private void setSqlMessages(Candidate candidate, ArrayList<SqlAnswersDTO> sqlAnswersAfterReview) {
        if (candidate != null && sqlAnswersAfterReview.size() > 0) {
            List<CandidateSqlTask> sqlTasksWithMessages = new ArrayList<>();
            for (CandidateSqlTask sqlTask : candidate.getCandidateSqlTasks()) {
                Optional<SqlAnswersDTO> answerOpt = sqlAnswersAfterReview
                        .stream()
                        .filter(t -> t.getSqlTaskId().equals(sqlTask.getSqlTask().getId()))
                        .findFirst();
                if (answerOpt.isPresent()) {
                    sqlTask.setMessage(answerOpt.get().getMessage());
                    sqlTask.setCorrect(answerOpt.get().getIsCorrect());
                }
                sqlTasksWithMessages.add(sqlTask);
            }
            candidate.setCandidateSqlTasks(sqlTasksWithMessages);
            candidateService.updateCandidate(candidate);
        }
    }

    private ArrayList<SqlAnswersDTO> processSqlAnswer(SqlAnswersDTO answerToProcess, ArrayList<SqlAnswersDTO> sqlAnswersAfterReview, Connection con) {

        SqlAnswersDTO answer = answerToProcess;
        ResultSet correctResultSet = null;
        ResultSet candidateResultSet = null;
        Statement statement = null;
        Statement statementCandidate = null;
        int nrRowsCorrectSet = 0;
        int nrRowsCandidateSet = 0;
        int nrColumnsCorrectStatement = 0;
        int nrColumnsCandidateStatement = 0;

        List<List<String>> listCorrectResults = new ArrayList<>();
        List<List<String>> listCandidateResults = new ArrayList<>();

        try {
            statement = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            statementCandidate = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.HOLD_CURSORS_OVER_COMMIT);

            correctResultSet = statement.executeQuery(answer.getCorrectStatement());
            candidateResultSet = statementCandidate.executeQuery(answer.getSqlAnswer());

            nrColumnsCorrectStatement = correctResultSet.getMetaData().getColumnCount();
            nrColumnsCandidateStatement = candidateResultSet.getMetaData().getColumnCount();

            //get nr rows correct ResultSet
            correctResultSet.last();
            nrRowsCorrectSet = correctResultSet.getRow();
            correctResultSet.beforeFirst();

            //get nr rows candidate ResultSet
            candidateResultSet.last();
            nrRowsCandidateSet = candidateResultSet.getRow();
            candidateResultSet.beforeFirst();

            if (nrColumnsCandidateStatement != nrColumnsCorrectStatement) {
                answer.setMessage("Nr. of columns is different. The statement is wrong.");
                sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
            } else if (nrRowsCandidateSet != nrRowsCorrectSet) {
                answer.setMessage("Nr. of rows is different. The statement is wrong.");
                sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
            } else {
                listCorrectResults = convertResultSetIntoList(correctResultSet, nrColumnsCorrectStatement);
                listCandidateResults = convertResultSetIntoList(candidateResultSet, nrColumnsCandidateStatement);
                if (!isContentDifferent(listCorrectResults, listCandidateResults, answer)) {
                    if (!answer.getAreColumnsNamed()) {
                        answer.setMessage("Candidate's statement is correct");
                        sqlAnswersAfterReview.add(getAnswerAfterReview(answer, true));
                    } else {
                        if (areColumnNamesTheSame(correctResultSet, candidateResultSet)) {
                            answer.setMessage("Candidate's statement is correct");
                        } else {
                            answer.setMessage("Result is correct, but column names are not the same or there was an error");
                        }
                        sqlAnswersAfterReview.add(getAnswerAfterReview(answer, true));
                    }
                } else {
                    answer.setMessage("Content is different. Statement is wrong");
                    sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
                }
            }
        } catch (SQLException e) {
            log.error("Error while processing candidates answer: {}", answer);
            answer.setMessage("Error while processing SQL answer");
            sqlAnswersAfterReview.add(getAnswerAfterReview(answer, false));
        } finally {
            try {
                if (correctResultSet != null) {
                    correctResultSet.close();
                }
                if (candidateResultSet != null) {
                    candidateResultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (statementCandidate != null) {
                    statementCandidate.close();
                }
            } catch (SQLException e) {
                log.error("Error while closing result sets");
            }
        }
        return sqlAnswersAfterReview;
    }

    private List<List<String>> convertResultSetIntoList(ResultSet resultSet, int nrColumns) {
        List<List<String>> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                ArrayList<String> rowList = new ArrayList<>();
                for (int i = 1; i <= nrColumns; i++) {
                    if (resultSet.getObject(i) != null) {
                        rowList.add(resultSet.getObject(i).toString());
                    }
                }
                list.add(rowList);
            }
        } catch (SQLException e) {
            log.error("Error while converting ResultSet into List");
        }
        return list;
    }

    private boolean areColumnNamesTheSame(ResultSet correctResultSet, ResultSet candidateResultSet) {
        try {
            int nrColumns = correctResultSet.getMetaData().getColumnCount();
            List<String> columnsCorrectStatement = new ArrayList<>();
            List<String> columnsCandidateStatement = new ArrayList<>();

            for (int i = 1; i <= nrColumns; i++) {
                columnsCorrectStatement.add(correctResultSet.getMetaData().getColumnName(i));
                columnsCandidateStatement.add(candidateResultSet.getMetaData().getColumnName(i));
            }
            if (columnsCandidateStatement.equals(columnsCorrectStatement)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error("Error while verifying columns names");
            return false;
        }
    }

    private boolean isContentDifferent(List<List<String>> correctList, List<List<String>> candidateList, SqlAnswersDTO answer) {
        boolean result = true;

        List<List<String>> sortedCorrectList = sortRows(correctList, answer.getAreRowsOrdered());
        List<List<String>> sortedCandidateList = sortRows(candidateList, answer.getAreRowsOrdered());

        /* compare content */
        basic_loop:
        for (List<String> row : sortedCorrectList) {
            int rowIndex = sortedCorrectList.indexOf(row);

            /* get this row in Candidate List */
            List<String> rowFromCandidateList = sortedCandidateList.get(rowIndex);

            /* compare 2 rows not taking into consideration the order of columns */
            for (String correctCellData : row) {
                boolean dataFound = false;
                for (String candidateCellData : rowFromCandidateList) {
                    if (correctCellData.equals(candidateCellData)) {
                        dataFound = true;
                        break;
                    }
                }
                /* stop all if the respective cell data was not found */
                if (!dataFound) {
                    /* stop processing if at least one cell was not found */
                    result = true;
                    break basic_loop;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    private List<List<String>> sortRows(List<List<String>> list, boolean areRowsSorted) {
        List<List<String>> sortedList = new ArrayList<>();

        //always sort columns
        for (List<String> row : list) {
            Collections.sort(row);
            sortedList.add(row);
        }
        //sort rows if not required by question
        if (!areRowsSorted) {
            Collections.sort(sortedList, new CustomComparator());
        }

        return sortedList;
    }

    private SqlAnswersDTO getAnswerAfterReview(SqlAnswersDTO answer, boolean result) {
        SqlAnswersDTO answerProcessed = answer;
        answer.setIsCorrect(result);
        return answerProcessed;
    }

    //endregion
}

class CustomComparator implements Comparator<List<String>> {
    @Override
    public int compare(List<String> o1,
                       List<String> o2) {
        String firstString_o1 = o1.get(0);
        String firstString_o2 = o2.get(0);
        return firstString_o1.compareTo(firstString_o2);
    }
}
