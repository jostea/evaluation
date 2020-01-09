package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.test.CandidateTestResultsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestReviewExecutorService {

    private final TestReviewService testReviewService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void executeReview(final String token) {
        executorService.submit(
                () -> {
                    log.info("START executing test review for the token: " + token);
                    CandidateTestResultsDTO results = testReviewService.reviewCandidateTest(token);
                    log.info("FINISH executing test review for the token: " + token);
                }
        );
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
