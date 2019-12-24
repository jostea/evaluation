package com.internship.evaluation.service;


import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.entity.StreamTime;
import com.internship.evaluation.model.entity.TestToken;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.StreamRepository;
import com.internship.evaluation.repository.StreamTimeRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimerService {

    private final CandidateRepository candidateRepository;

    private final StreamRepository streamRepository;

    private final StreamTimeRepository streamTimeRepository;

    private final TestTokenRepository testTokenRepository;

    private String getLeftTime(String token) {
        Candidate candidate = getCandidateByToken(token);
        Stream stream = candidate.getStream();
        Optional<StreamTime> streamTimeOptional = streamTimeRepository.findFirstByStream_Id(stream.getId());
        if (streamTimeOptional.isPresent()) {
            StreamTime streamTime = streamTimeOptional.get();
            Integer timeTest = streamTime.getTimeTest();
            while (candidate.getDateTestStarted().equals(LocalDateTime.now())) {
            }

        }
        return "";
    }

    public Candidate getCandidateByToken(String token) {
        Candidate candidate = null;
        Optional<TestToken> entityOpt = testTokenRepository.findFirstByToken(token);
        if (entityOpt.isPresent()) {
            candidate = entityOpt.get().getCandidate();
        }
        return candidate;
    }
}
