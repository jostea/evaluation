package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.TestTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    private final CandidateService candidateService;

    @PostMapping(value = "/testStart")
    public ResponseEntity<String> startTest(@RequestParam String thd_i8) {
        Candidate candidate = tokenService.getCandidateByToken(thd_i8);
        if (candidate != null) {

            //if test has already been started - provide the message
            if (candidate.getTestStatus().equals(TestStatusEnum.TEST_STARTED)){
                log.warn("Candidate with id " + candidate.getId() + " has tried to restart the test");

                //Construct URI
                String evalPortalHost = env.getProperty("eval_portal.host");
                UriComponents uriComponents = UriComponentsBuilder.newInstance()
                        .scheme("http")
                        .host(evalPortalHost)
                        .path("/test")
                        .query("thd_i8={keyword}")
                        .buildAndExpand(thd_i8);

                return new ResponseEntity<>(uriComponents.toUriString(),
                        HttpStatus.BAD_REQUEST);
            } else {
                try {
                    candidate.setDateTestStarted(Timestamp.valueOf(LocalDateTime.now()));
                    candidate.setTestStatus(TestStatusEnum.TEST_STARTED);
                    candidateService.updateCandidate(candidate);
                    return new ResponseEntity<>("Test is started", HttpStatus.OK);
                } catch (Exception e) {
                    log.error("Error while starting the test for candidate " + candidate.toString()
                            + "\nStack trace: " + e.getStackTrace());
                    return new ResponseEntity<>("Error while starting the test", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            log.warn("Failed attempt to start the test: candidate was not found with token " + thd_i8);
            return new ResponseEntity<>("Error while starting the test", HttpStatus.BAD_REQUEST);
        }
    }
}
