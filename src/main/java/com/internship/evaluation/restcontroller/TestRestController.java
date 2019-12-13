package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.GenerateTestService;
import com.internship.evaluation.service.TestTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/testsrest")
public class TestRestController {

    @Autowired
    private Environment env;

    private final TestTokenService tokenService;
    private final GenerateTestService generateTestService;

    @PostMapping(value = "/testStart")
    public ResponseEntity<?> startTest(@RequestParam String thd_i8) {
        Candidate candidate = tokenService.getCandidateByToken(thd_i8);
        ResponseEntity response = null;
        if (candidate != null) {
            //if test has already been started - provide the message
            if (candidate.getTestStatus().equals(TestStatusEnum.TEST_STARTED)){
                log.warn("Candidate with id " + candidate.getId() + " has tried to restart the test");
                response = new ResponseEntity<>("Test is already started", HttpStatus.OK);
            } else if (candidate.getTestStatus().equals(TestStatusEnum.WAITING_ACTIVATION)){
                try {
                    candidate.setDateTestStarted(Timestamp.valueOf(LocalDateTime.now()));
                    candidate.setTestStatus(TestStatusEnum.TEST_STARTED);
                    candidateService.updateCandidate(candidate);
                    response = new ResponseEntity<>("Test has been started", HttpStatus.OK);
                    return new ResponseEntity<>(generateTestService.generateTest(thd_i8), HttpStatus.OK);
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
}
