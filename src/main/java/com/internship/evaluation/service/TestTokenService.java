package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.test_token.TestTokenDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.TestToken;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestTokenService {

    private final TestTokenRepository testTokenRepository;
    private final CandidateRepository candidateRepository;

    //to generate test_token
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public void generateNewTestToken(Long candId) {
        Optional<Candidate> candOptional = candidateRepository.findById(candId);
        Candidate candidate = null;
        if (candOptional.isPresent()) {
            candidate = candOptional.get();
        }

        TestToken newToken = new TestToken();
        newToken.setCandidate(candidate);
        newToken.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        newToken.setToken(base64Encoder.encodeToString(randomBytes));

        //save new test_token
        testTokenRepository.save(newToken);
    }

    public Timestamp getTokenDateCreated(String token) {
        Optional<TestToken> entityOpt = testTokenRepository.findFirstByToken(token);
        TestToken entity = null;
        if (entityOpt.isPresent()) {
            entity = entityOpt.get();
            return entity.getDateCreated();
        } else {
            return null;
        }
    }

    public TestTokenDTO getTestTokenByToken(String token) {
        TestTokenDTO tokenDTO = null;
        Optional<TestToken> entityOpt = testTokenRepository.findFirstByToken(token);
        if (entityOpt.isPresent()) {
            tokenDTO = new TestTokenDTO(entityOpt.get());
        }
        return tokenDTO;
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
