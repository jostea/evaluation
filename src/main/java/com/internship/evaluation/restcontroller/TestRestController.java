package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.generate_test.GenerateTestDTO;
import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import com.internship.evaluation.model.dto.generate_test.SqlCandidateAnswerDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.GenerateTestService;
import com.internship.evaluation.service.RestAdminService;
import com.internship.evaluation.service.TestTokenService;
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

    @PostMapping(value = "/testStart")
    public ResponseEntity<?> startTest(@RequestParam String thd_i8) {
        Candidate candidate = tokenService.getCandidateByToken(thd_i8);
        ResponseEntity response = null;
        if (candidate != null) {
            //if test has already been started - provide the message
            if (candidate.getTestStatus().equals(TestStatusEnum.TEST_STARTED)){
                log.warn("Candidate with id " + candidate.getId() + " has tried to restart the test");
                GenerateTestDTO existingTest = generateTestService.getExistingTest(candidate);
                return new ResponseEntity<>(existingTest, HttpStatus.ACCEPTED);
            } else if (candidate.getTestStatus().equals(TestStatusEnum.WAITING_ACTIVATION)){
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

    @GetMapping(value = "/getSqlImage/{idSqlGroup}")
    public ResponseEntity getSqlImage(@PathVariable Long idSqlGroup){
        ResponseEntity responseAdmin = restAdminService.getSqlImage(idSqlGroup);
        return responseAdmin;
    }

    @PostMapping(value = "/saveSqlAnswers")
    public ResponseEntity saveSqlAnswers(@RequestBody SqlCandidateAnswerDTO dto){
        ArrayList<SqlAnswersDTO> sqlAnswers = dto.getAnswers();
        Candidate candidate = tokenService.getCandidateByToken(dto.getToken());
        try {
            candidateService.saveCandidateSqlAnswers(candidate, dto);
            log.info("Sql answers of Candidate with the token [" + dto.getToken() + "] were saved.");
            return new ResponseEntity("SQL answers were successfully processed.", HttpStatus.OK);
        } catch (Exception e){
            log.error("Error while saving SQL answers of candidate with the token [" + dto.getToken() + "].");
            return new ResponseEntity("Error while processing SQL answers.", HttpStatus.BAD_REQUEST);
        }
    }
}
