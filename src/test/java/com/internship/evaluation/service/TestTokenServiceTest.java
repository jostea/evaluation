package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.test_token.TestTokenDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.TestToken;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestTokenServiceTest {
    @Mock
    private TestTokenRepository testTokenRepository;
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private TestTokenService testTokenService;

    @Test
    public void shouldReturnedTokenDateCreated() {
        Timestamp expectedTime = (Timestamp.valueOf(LocalDateTime.of(2019, 8, 5, 5, 50, 55, 10)));
        TestToken mockedTestToken = TestToken.builder()
                .token("SomeToken")
                .dateCreated(Timestamp.valueOf(LocalDateTime.of(2019, 8, 5, 5, 50, 55, 10)))
                .build();
        when(testTokenRepository.findFirstByToken("SomeToken")).thenReturn(Optional.of(mockedTestToken));
        Timestamp returnedToken = testTokenService.getTokenDateCreated("SomeToken");

        verify(testTokenRepository).findFirstByToken("SomeToken");
        assertThat(returnedToken).isEqualTo(expectedTime);
    }

    @Test
    public void shouldReturnedNullWhileGetTokenDateCreated() {
        String token = "token";
        when(testTokenRepository.findFirstByToken(token)).thenReturn(Optional.empty());
        Timestamp returnedResult = testTokenService.getTokenDateCreated(token);
        verify(testTokenRepository).findFirstByToken(token);
        assertThat(returnedResult).isEqualTo(null);
    }

    @Test
    public void shouldReturnedTestTokenByToken() {
        String token = "token";
        TestToken mockedTestToken = TestToken.builder()
                .id(1L)
                .token(token)
                .dateCreated(Timestamp.valueOf(LocalDateTime.of(2019, 8, 5, 5, 50, 55, 10)))
                .isActive(true)
                .candidate(Candidate.builder().id(1L).build())
                .build();

        TestTokenDTO expectedResult = TestTokenDTO.builder()
                .id(1L)
                .candidateId(1L)
                .token(token)
                .dateCrated(Timestamp.valueOf(LocalDateTime.of(2019, 8, 5, 5, 50, 55, 10)))
                .isActive(true)
                .build();

        when(testTokenRepository.findFirstByToken(token)).thenReturn(Optional.of(mockedTestToken));
        TestTokenDTO returnedResult = testTokenService.getTestTokenByToken(token);
        verify(testTokenRepository).findFirstByToken(token);
        assertThat(returnedResult).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnedCandidateByToken() {
        Candidate expectedCandidate = Candidate.builder()
                .id(1L)
                .firstName("SomeName")
                .build();
        TestToken mockedTestToken = TestToken.builder()
                .id(1L)
                .token("token")
                .dateCreated(Timestamp.valueOf(LocalDateTime.of(2019, 8, 5, 5, 50, 55, 10)))
                .isActive(true)
                .candidate(Candidate.builder().id(1L).firstName("SomeName").build())
                .build();

        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(mockedTestToken));
        Candidate returnedResult = testTokenService.getCandidateByToken("token");
        verify(testTokenRepository).findFirstByToken("token");
        assertThat(returnedResult).isEqualTo(expectedCandidate);
    }
}
