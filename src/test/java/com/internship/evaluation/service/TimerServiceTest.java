package com.internship.evaluation.service;

import com.internship.evaluation.exception.TimeOut;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.StreamTimeRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimerServiceTest {
    @Mock
    private StreamTimeRepository streamTimeRepository;
    @Mock
    private TestTokenRepository testTokenRepository;
    @Mock
    private TestTokenService testTokenService;
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private TimerService timerService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReturnedLeftTime() throws TimeOut {
        LocalDateTime localDateTime = LocalDateTime.now();
        Stream stream = Stream.builder()
                .name("Java")
                .discipline(Discipline.builder()
                        .name("Development")
                        .build())
                .build();
        StreamTime mockedStreamTime = StreamTime.builder()
                .stream(stream)
                .timeTest(60)
                .stream(stream)
                .build();
        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .internship(Internship.builder().name("TestNameInternship").build())
                .firstName("Johny")
                .lastName("Bravo")
                .dateTestStarted(Timestamp.valueOf(localDateTime))
                .stream(stream)
                .testStatus(TestStatusEnum.TEST_STARTED)
                .build();

        int timeTest = 0;
        LocalDateTime timeForExecuteTest = mockedCandidate.getDateTestStarted().toLocalDateTime().plusMinutes(mockedStreamTime.getTimeTest());
        Duration diff = Duration.between(localDateTime, timeForExecuteTest);
        timeTest = (int) diff.toMillis() / 1000;

        when(testTokenService.getCandidateByToken("token")).thenReturn(mockedCandidate);
        when(streamTimeRepository.findFirstByStream_Id(stream.getId())).thenReturn(Optional.of(mockedStreamTime));
        Integer returnedTime = timerService.getLeftTime("token");
        assertThat(timeTest).isEqualTo(returnedTime + 1);
    }

    @Test
    public void shouldThrowExceptionTimeOut() throws TimeOut {
        exception.expect(TimeOut.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        Stream stream = Stream.builder()
                .name("Java")
                .discipline(Discipline.builder()
                        .name("Development")
                        .build())
                .build();
        StreamTime mockedStreamTime = StreamTime.builder()
                .stream(stream)
                .timeTest(60)
                .stream(stream)
                .build();
        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .internship(Internship.builder().name("TestNameInternship").build())
                .firstName("Johny")
                .lastName("Bravo")
                .dateTestStarted(Timestamp.valueOf(localDateTime.minusMinutes(70)))
                .stream(stream)
                .testStatus(TestStatusEnum.TEST_STARTED)
                .build();

        when(testTokenService.getCandidateByToken("token")).thenReturn(mockedCandidate);
        when(streamTimeRepository.findFirstByStream_Id(stream.getId())).thenReturn(Optional.of(mockedStreamTime));
        timerService.getLeftTime("token");
    }



}
