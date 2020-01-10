package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.model.dto.candidate.CandidateStartTestDTO;
import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import com.internship.evaluation.model.dto.generate_test.SqlCandidateAnswerDTO;
import com.internship.evaluation.model.dto.save_code.SaveCodeAnswerDTOFromUI;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveCustomAnswerDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveMultiTaskDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveSingleTaskDTO;
import com.internship.evaluation.model.dto.test_token.TestTokenDTO;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.repository.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidateServiceTest {
    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private StreamRepository streamRepository;

    @Mock
    private InternshipRepository internshipRepository;

    @Mock
    private TestTokenService testTokenService;

    @Mock
    private StreamTimeRepository streamTimeRepository;

    @Mock
    private CandidateSqlTaskRepository candidateSqlTaskRepository;

    @Mock
    private CandidateMultiTaskRepository candidateMultiTaskRepository;

    @Mock
    private AnswersOptionRepository answersOptionRepository;

    @Mock
    private CandidateSingleTaskRepository candidateSingleTaskRepository;

    @Mock
    private CandidateCustomTaskRepository candidateCustomTaskRepository;

    @Mock
    private CandidateCodeTaskRepository candidateCodeTaskRepository;

    @Mock
    private CodeTaskRepository codeTaskRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldGetCandidateStartTestByToken() {
        String token = "someToken";
        Stream stream = Stream.builder()
                .name("Java")
                .discipline(Discipline.builder()
                        .name("Development")
                        .build())
                .build();

        TestTokenDTO mockedTestToken = TestTokenDTO.builder()
                .id(1L)
                .token(token)
                .candidateId(1L)
                .dateCrated(Timestamp.valueOf(LocalDateTime.now()))
                .isActive(true)
                .build();

        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .internship(Internship.builder().name("TestNameInternship").build())
                .firstName("Johny")
                .lastName("Bravo")
                .stream(stream)
                .testStatus(TestStatusEnum.TEST_STARTED)
                .build();

        StreamTime mockedStreamTime = StreamTime.builder()
                .stream(stream)
                .timeTest(60)
                .stream(stream)
                .build();

        CandidateStartTestDTO expectedCandidateStartTestDTO = CandidateStartTestDTO.builder()
                .internshipName("TestNameInternship")
                .testStatusEnum(TestStatusEnum.TEST_STARTED)
                .testTime(60)
                .disciplineName("Development")
                .streamName("Java")
                .firstName("Johny")
                .lastName("Bravo")
                .build();

        when(testTokenService.getTestTokenByToken(token)).thenReturn(mockedTestToken);
        when(candidateRepository.getOne(mockedTestToken.getCandidateId())).thenReturn(mockedCandidate);
        when(streamTimeRepository.findFirstByStream_Id(mockedCandidate.getStream().getId())).thenReturn(Optional.of(mockedStreamTime));
        CandidateStartTestDTO returnedResult = candidateService.getCandidateStartTestByToken(token);

        verify(testTokenService).getTestTokenByToken(token);
        verify(candidateRepository).getOne(mockedTestToken.getCandidateId());
        verify(streamTimeRepository).findFirstByStream_Id(mockedCandidate.getStream().getId());
        assertThat(returnedResult).isEqualTo(expectedCandidateStartTestDTO);
    }

    @Test
    public void shouldAddCandidate() {
        CandidateRegistrationDTO candidateRegistrationDTO =
                CandidateRegistrationDTO.builder()
                        .email("somemail@gmail.com")
                        .firstName("Johny")
                        .lastName("Bravo")
                        .internshipId(1L)
                        .phone("373789456123")
                        .streamId(1L)
                        .build();

        Stream mockedStream = Stream.builder()
                .name("Java")
                .discipline(Discipline.builder()
                        .name("Development")
                        .build())
                .build();

        Internship mockedInternship = Internship.builder()
                .id(1L)
                .name("SomeName")
                .build();

        Candidate mockedCandidate = new Candidate();
        mockedCandidate.setFirstName("Johny");
        mockedCandidate.setLastName("Bravo");
        mockedCandidate.setEmail("somemail@gmail.com");
        mockedCandidate.setStream(mockedStream);
        mockedCandidate.setInternship(mockedInternship);

        Candidate mockedCandidate2 = new Candidate();
        mockedCandidate2.setId(1L);
        mockedCandidate2.setFirstName("Johny");
        mockedCandidate2.setLastName("Bravo");
        mockedCandidate2.setEmail("somemail@gmail.com");
        mockedCandidate2.setStream(mockedStream);
        mockedCandidate2.setInternship(mockedInternship);

        List<Candidate> candidates = new ArrayList<>();
        when(candidateRepository.findAll()).thenReturn(candidates);
        when(streamRepository.findById(candidateRegistrationDTO.getStreamId())).thenReturn(Optional.of(mockedStream));
        when(internshipRepository.findById(candidateRegistrationDTO.getInternshipId())).thenReturn(Optional.of(mockedInternship));
        when(candidateRepository.saveAndFlush(any(Candidate.class))).thenReturn(mockedCandidate);

        Long returnedResult = candidateService.addCandidate(candidateRegistrationDTO);
        verify(candidateRepository).findAll();
        verify(streamRepository).findById(candidateRegistrationDTO.getStreamId());
        verify(internshipRepository).findById(candidateRegistrationDTO.getInternshipId());
        verify(candidateRepository).saveAndFlush(any(Candidate.class));
        assertThat(returnedResult).isEqualTo(mockedCandidate.getId());
    }

    @Test
    public void shouldUpdateCandidate() {
        Candidate candidate = Candidate.builder()
                .id(1L)
                .firstName("Johny")
                .lastName("Bravo")
                .email("somemail@gmail.com")
                .build();
        candidateService.updateCandidate(candidate);
        verify(candidateRepository).save(candidate);
    }

    @Test
    public void shouldSaveCandidateCodeAnswer() throws Exception {
        SaveCodeAnswerDTOFromUI saveCodeAnswerDTOFromUI = new SaveCodeAnswerDTOFromUI();
        saveCodeAnswerDTOFromUI.setCode("SomeCode");
        saveCodeAnswerDTOFromUI.setId(1L);
        saveCodeAnswerDTOFromUI.setToken("someToken");
        CodeTask mockedCodeTask = CodeTask.builder().id(1L).build();
        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .firstName("Johny")
                .lastName("Bravo")
                .email("somemail@gmail.com")
                .build();
        CandidateCodeTask mockedCandidateCodeTask = CandidateCodeTask.builder()
                .id(1L).build();
        when(codeTaskRepository.findById(saveCodeAnswerDTOFromUI.getId())).thenReturn(Optional.of(mockedCodeTask));
        when(testTokenService.getCandidateByToken(saveCodeAnswerDTOFromUI.getToken())).thenReturn(mockedCandidate);
        when(candidateCodeTaskRepository.findByCodeTaskAndCandidate(mockedCodeTask, mockedCandidate)).thenReturn(mockedCandidateCodeTask);

        candidateService.saveCandidateCodeAnswer(saveCodeAnswerDTOFromUI);
        verify(candidateCodeTaskRepository).save(mockedCandidateCodeTask);
    }

    @Test
    public void shouldSaveCandidateCustomAnswer() throws Exception {
        SaveCustomAnswerDTO saveCustomAnswerDTO = new SaveCustomAnswerDTO();
        saveCustomAnswerDTO.setAnswerContent("someText");
        saveCustomAnswerDTO.setTaskId(1L);
        CandidateCustomTask candidateCustomTask = new CandidateCustomTask();
        when(candidateCustomTaskRepository.findById(saveCustomAnswerDTO.getTaskId())).thenReturn(Optional.of(candidateCustomTask));
        candidateService.saveCandidateCustomAnswer(saveCustomAnswerDTO);
        verify(candidateCustomTaskRepository).save(candidateCustomTask);
    }

    @Test
    public void shouldSaveCandidateSingleAnswer() throws Exception {
        SaveSingleTaskDTO taskUI = new SaveSingleTaskDTO();
        taskUI.setCandidateTaskId(1L);
        taskUI.setSingleAnswer(1L);
        CandidateSingleTask candidateSingleTask = new CandidateSingleTask();
        when(candidateSingleTaskRepository.findById(taskUI.getCandidateTaskId())).thenReturn(Optional.of(candidateSingleTask));
        when(answersOptionRepository.findById(taskUI.getSingleAnswer())).thenReturn(Optional.of(new AnswersOption()));

        candidateService.saveCandidateSingleAnswer(taskUI);
        verify(candidateSingleTaskRepository).findById(1L);
        verify(answersOptionRepository).findById(1L);
    }


    @Test
    public void shouldSaveCandidateOneSqlAnswer() {

        SqlCandidateAnswerDTO sqlDTO = new SqlCandidateAnswerDTO();
        sqlDTO.setToken("token");
        ArrayList<SqlAnswersDTO> sqlAnswersDTOS = new ArrayList<>();

        SqlAnswersDTO sqlAnswersDTO = new SqlAnswersDTO();
        sqlAnswersDTO.setMessage("someMessage");
        sqlAnswersDTO.setAreColumnsNamed(true);
        sqlAnswersDTO.setAreRowsOrdered(true);
        sqlAnswersDTO.setCorrectStatement("someText");
        sqlAnswersDTO.setSqlAnswer("someAnswer");
        sqlAnswersDTO.setSqlTaskId(1L);
        sqlAnswersDTOS.add(sqlAnswersDTO);

        sqlDTO.setAnswers(sqlAnswersDTOS);
        ArrayList<CandidateSqlTask> sqlTasksAssignedToCandidate = new ArrayList<>();
        sqlTasksAssignedToCandidate.add(CandidateSqlTask.builder().sqlTask(SqlTask.builder().id(1L).build()).build());

        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .candidateSqlTasks(sqlTasksAssignedToCandidate)
                .firstName("Johny")
                .lastName("Bravo")
                .email("somemail@gmail.com")
                .build();

        candidateService.saveCandidateOneSqlAnswer(mockedCandidate, sqlDTO);
    }

    @Test
    public void shouldSaveCandidateMultiAnswers() throws Exception {
        SaveMultiTaskDTO multiTaskDTO = new SaveMultiTaskDTO();
        multiTaskDTO.setCandidateTaskId(1L);

        List<Long> multiTaskAnswers = new ArrayList<>();
        multiTaskAnswers.add(1L);
        multiTaskAnswers.add(2L);
        multiTaskAnswers.add(3L);

        multiTaskDTO.setMultiTaskAnswers(multiTaskAnswers);

        CandidateMultiTask mockedCandidateMultiTask = CandidateMultiTask.builder().id(1L).build();

        when(candidateMultiTaskRepository.findById(multiTaskDTO.getCandidateTaskId())).thenReturn(Optional.of(mockedCandidateMultiTask));

        for (Long id : multiTaskDTO.getMultiTaskAnswers()) {
            when(answersOptionRepository.findById(id)).thenReturn(Optional.of(AnswersOption.builder().build()));
        }

        candidateService.saveCandidateMultiAnswers(multiTaskDTO);

        verify(candidateMultiTaskRepository).findById(multiTaskDTO.getCandidateTaskId());

        for (Long id : multiTaskDTO.getMultiTaskAnswers()) {
            verify(answersOptionRepository).findById(id);
        }

        verify(candidateMultiTaskRepository).save(mockedCandidateMultiTask);
    }
}
