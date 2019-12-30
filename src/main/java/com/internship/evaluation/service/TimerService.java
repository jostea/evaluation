package com.internship.evaluation.service;

import com.internship.evaluation.exception.TimeOut;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.repository.StreamTimeRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimerService {

    private final StreamTimeRepository streamTimeRepository;

    private final TestTokenRepository testTokenRepository;

    private final TestTokenService testTokenService;

    public Integer getLeftTime(String token) throws TimeOut {
        Candidate candidate = testTokenService.getCandidateByToken(token);
        Stream stream = candidate.getStream();
        Integer timeTest = 0;
        Optional<StreamTime> streamTimeOptional = streamTimeRepository.findFirstByStream_Id(stream.getId());
        if (streamTimeOptional.isPresent()) {
            StreamTime streamTime = streamTimeOptional.get();
            timeTest = streamTime.getTimeTest();
            LocalDateTime timeForExecuteTest = candidate.getDateTestStarted().toLocalDateTime().plusMinutes(timeTest);
            if (timeForExecuteTest.compareTo(LocalDateTime.now()) > 0) {
                Duration diff = Duration.between(LocalDateTime.now(), timeForExecuteTest);
                timeTest = (int) diff.toMillis() / 1000;
            } else {
                throw new TimeOut();
            }
        }
        return timeTest;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void findTimeOutTasks() {
        List<TestToken> testTokens = testTokenRepository.findAll();
        if (testTokens.size() != 0)
            for (TestToken token : testTokens) {
                Candidate candidate = testTokenService.getCandidateByToken(token.getToken());
                Stream stream = candidate.getStream();
                Optional<StreamTime> streamTimeOptional = streamTimeRepository.findFirstByStream_Id(stream.getId());
                if (streamTimeOptional.isPresent()) {
                    StreamTime streamTime = streamTimeOptional.get();
                    Integer timeTest = streamTime.getTimeTest();
                    LocalDateTime haveTime = candidate.getDateTestStarted().toLocalDateTime().plusMinutes(timeTest);
                    if ((haveTime.compareTo(LocalDateTime.now()) < 0)) {
                        candidate.setTestStatus(TestStatusEnum.TEST_FINISHED);
                        token.setActive(false);
                        token.setCandidate(candidate);
                        testTokenRepository.save(token);
                    }
                }
            }
    }
}
