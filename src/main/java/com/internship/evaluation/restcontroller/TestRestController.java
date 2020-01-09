package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.generate_test.GenerateTestDTO;
import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import com.internship.evaluation.model.dto.generate_test.SqlCandidateAnswerDTO;
import com.internship.evaluation.model.dto.save_code.SaveCodeAnswerDTOFromUI;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveCustomAnswerDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveMultiTaskDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveSingleTaskDTO;
import com.internship.evaluation.model.dto.test.CandidateTestResultsDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.CandidateSqlTask;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/testsrest")
public class TestRestController {

    @Autowired
    private Environment env;

    private final TestTokenService tokenService;
    private final GenerateTestService generateTestService;
    private final CandidateService candidateService;
    private final RestAdminService restAdminService;
    private final TestReviewService testReviewService;
    private final NotificationService notificationService;

    @PostMapping(value = "/testStart")
    public ResponseEntity<?> startTest(@RequestParam String thd_i8) {
        Candidate candidate = tokenService.getCandidateByToken(thd_i8);
        ResponseEntity response = null;
        if (candidate != null) {
            //if test has already been started - provide the message
            if (candidate.getTestStatus() == TestStatusEnum.TEST_STARTED) {
                log.warn("Candidate with id " + candidate.getId() + " has tried to restart the test");
                GenerateTestDTO existingTest = generateTestService.getExistingTest(candidate);
                return new ResponseEntity<>(existingTest, HttpStatus.ACCEPTED);
            } else if (candidate.getTestStatus() == TestStatusEnum.WAITING_ACTIVATION) {
                try {
                    GenerateTestDTO currentTest = generateTestService.generateTest(thd_i8);
                    candidate.setDateTestStarted(Timestamp.valueOf(LocalDateTime.now()));
                    candidate.setTestStatus(TestStatusEnum.TEST_STARTED);
                    candidateService.updateCandidate(candidate);
                    return new ResponseEntity<>(currentTest, HttpStatus.OK);
                } catch (Exception e) {
                    log.error("Error while starting the test for candidate " + candidate.toString() + "\nStack trace: " + e.getStackTrace());
                    return new ResponseEntity<>("Error while starting the test", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            log.warn("Attempt to reaccess test with token " + thd_i8 + ". Test was finished.");
            response = new ResponseEntity<>("Test is already finished", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping(value = "/testFinish")
    public ResponseEntity<?> finishTest(@RequestParam String thd_i8) {
        Candidate candidateToFinish = tokenService.getCandidateByToken(thd_i8);
        ResponseEntity response = null;
        try {
            if (candidateToFinish != null) {
                candidateToFinish.setTestStatus(TestStatusEnum.TEST_FINISHED);
                candidateToFinish.setDateTestFinished(Timestamp.valueOf(LocalDateTime.now()));
                candidateService.updateCandidate(candidateToFinish);
                log.info("Candidate with token {} finished the test", thd_i8);
                response = new ResponseEntity("Test is finished", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error while finishing the test of candidate with token {}", thd_i8);
            response = new ResponseEntity("Error while finishing the test", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping(value = "/getSqlImage/{idSqlGroup}")
    public ResponseEntity getSqlImage(@PathVariable Long idSqlGroup) {
        ResponseEntity responseAdmin = restAdminService.getSqlImage(idSqlGroup);
        return responseAdmin;
    }

    @PostMapping(value = "/saveSqlAnswers")
    public ResponseEntity saveSqlAnswers(@RequestBody SqlCandidateAnswerDTO dto) {
        ArrayList<SqlAnswersDTO> sqlAnswers = dto.getAnswers();
        Candidate candidate = tokenService.getCandidateByToken(dto.getToken());
        try {
            candidateService.saveCandidateSqlAnswers(candidate, dto);
            log.info("Sql answers of Candidate with the token [" + dto.getToken() + "] were saved.");
            return new ResponseEntity("SQL answers were successfully processed.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while saving SQL answers of candidate with the token [" + dto.getToken() + "].");
            return new ResponseEntity("Error while processing SQL answers.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveMultiAnswers")
    public ResponseEntity<?> saveMultiTask(@RequestBody SaveMultiTaskDTO saveMultiTaskDTO) {
        try {
            candidateService.saveCandidateMultiAnswers(saveMultiTaskDTO);
            return new ResponseEntity<>("Answers saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveSingleAnswer")
    public ResponseEntity<?> saveSingleTask(@RequestBody SaveSingleTaskDTO saveSingleTaskDTO) {
        try {
            candidateService.saveCandidateSingleAnswer(saveSingleTaskDTO);
            return new ResponseEntity<>("Answer saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveCustomAnswer")
    public ResponseEntity<?> saveCustomTask(@RequestBody SaveCustomAnswerDTO saveCustomAnswerDTO) {
        try {
            candidateService.saveCandidateCustomAnswer(saveCustomAnswerDTO);
            return new ResponseEntity<>("Answer saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/saveOneSqlAnswer")
    public ResponseEntity saveOneSqlAnswers(@RequestBody SqlCandidateAnswerDTO dto) {
        Candidate candidate = tokenService.getCandidateByToken(dto.getToken());
        try {
            candidateService.saveCandidateOneSqlAnswer(candidate, dto);
            log.info("One Sql answer of Candidate with the token [" + dto.getToken() + "] was saved.");
            return new ResponseEntity("One SQL answer was successfully processed.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while saving one SQL answer of candidate with the token [" + dto.getToken() + "].");
            return new ResponseEntity("Error while processing one SQL answer.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getSqlAnswers/{token}")
    public ResponseEntity<List<SqlAnswersDTO>> getSqlAnswers(@PathVariable("token") String tok) {
        try {
            Candidate candidate = tokenService.getCandidateByToken(tok);
            ArrayList<SqlAnswersDTO> sqlAnswersFromDB = new ArrayList<>();
            if (candidate != null) {
                for (CandidateSqlTask candidateSqlTask : candidate.getCandidateSqlTasks()) {
                    SqlAnswersDTO sqlAnswersDTO = new SqlAnswersDTO(candidateSqlTask);
                    sqlAnswersFromDB.add(sqlAnswersDTO);
                }
            }
            log.info("Sql answers have been extracted from DB for the candidate with token " + tok);
            return new ResponseEntity<>(sqlAnswersFromDB, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while getting sql answers for the candidate with token " + tok);
            return new ResponseEntity("Error while reviewing candidate's answers", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reEvaluateCandidate/{token}")
    public ResponseEntity<?> reEvaluateCandidate(@PathVariable("token") String token) {
        try {
            testReviewService.reviewCandidateTest(token);
            log.info("The candidate has been re-evaluated.");
            return new ResponseEntity<>("Results are reviewed", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while trying to re-evaluate the candidate with token {}", token);
            return new ResponseEntity("Error while re-evaluating candidate's answers", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCandidateResults/{token}")
    public ResponseEntity<?> getCandidateResults(@PathVariable("token") String token) {
        try {
            testReviewService.reviewCandidateTest(token);
            log.info("Test results of candidate with token {} where provided");
            notificationService.sendTestReviewNotification(tokenService.getCandidateByToken(token).getId());
            return new ResponseEntity<>("Results are reviewed", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while trying to get test results for the candidate with token {}", token);
            return new ResponseEntity("Error while reviewing candidate's answers", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveCodeAnswer")
    public ResponseEntity<?> saveCodeAnswer(@RequestBody SaveCodeAnswerDTOFromUI saveCodeAnswerDTO) {
        try {
            candidateService.saveCandidateCodeAnswer(saveCodeAnswerDTO);
            return new ResponseEntity<>("Code task saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Code task could not be saved", HttpStatus.BAD_REQUEST);
        }
    }
}
