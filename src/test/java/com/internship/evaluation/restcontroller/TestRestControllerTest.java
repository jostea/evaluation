package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.generate_test.SqlCandidateAnswerDTO;
import com.internship.evaluation.model.dto.save_code.SaveCodeAnswerDTOFromUI;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveCustomAnswerDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveMultiTaskDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveSingleTaskDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestRestControllerTest {
    @Mock
    private TestTokenService tokenService;

    @Mock
    private GenerateTestService generateTestService;

    @Mock
    private CandidateService candidateService;

    @Mock
    private RestAdminService restAdminService;

    @Mock
    private TestReviewService testReviewService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TestRestController testRestController;

    @Test
    public void shouldFinishTest() {
        String token = "someThing";
        Candidate candidate = Candidate.builder().id(1L).build();
        when(tokenService.getCandidateByToken(token)).thenReturn(candidate);
        ResponseEntity<?> returnedResult = testRestController.finishTest(token);
        verify(tokenService).getCandidateByToken(token);
        verify(candidateService).updateCandidate(candidate);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(returnedResult.getBody()).isEqualTo("Test is finished");
    }

    @Test
    public void shouldSaveSqlAnswers() {
        SqlCandidateAnswerDTO sqlCandidateAnswerDTO = new SqlCandidateAnswerDTO();
        sqlCandidateAnswerDTO.setToken("someThing");
        Candidate candidate = Candidate.builder().id(1L).build();
        when(tokenService.getCandidateByToken(sqlCandidateAnswerDTO.getToken())).thenReturn(candidate);
        ResponseEntity<?> returnedResult = testRestController.saveSqlAnswers(sqlCandidateAnswerDTO);
        verify(tokenService).getCandidateByToken(sqlCandidateAnswerDTO.getToken());
        verify(candidateService).saveCandidateSqlAnswers(candidate, sqlCandidateAnswerDTO);

        assertThat(returnedResult.getBody()).isEqualTo("SQL answers were successfully processed.");
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldSaveMultiTask() throws Exception {
        SaveMultiTaskDTO multiTaskDTO = new SaveMultiTaskDTO();
        ResponseEntity<?> returnedResult = testRestController.saveMultiTask(multiTaskDTO);
        verify(candidateService).saveCandidateMultiAnswers(multiTaskDTO);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(returnedResult.getBody()).isEqualTo("Answers saved successfully");
    }

    @Test
    public void shouldSaveSingleTask() throws Exception {
        SaveSingleTaskDTO singleTaskDTO = new SaveSingleTaskDTO();
        ResponseEntity<?> returnedResult = testRestController.saveSingleTask(singleTaskDTO);
        verify(candidateService).saveCandidateSingleAnswer(singleTaskDTO);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(returnedResult.getBody()).isEqualTo("Answer saved successfully");
    }

    @Test
    public void shouldSaveCustomTask() throws Exception {
        SaveCustomAnswerDTO customAnswerDTO = new SaveCustomAnswerDTO();
        ResponseEntity<?> returnedResult = testRestController.saveCustomTask(customAnswerDTO);
        verify(candidateService).saveCandidateCustomAnswer(customAnswerDTO);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(returnedResult.getBody()).isEqualTo("Answer saved successfully");
    }

    @Test
    public void shouldSaveOneSqlAnswer() {
        SqlCandidateAnswerDTO sqlCandidateAnswerDTO = new SqlCandidateAnswerDTO();
        sqlCandidateAnswerDTO.setToken("someThing");
        Candidate candidate = Candidate.builder().id(1L).build();
        when(tokenService.getCandidateByToken(sqlCandidateAnswerDTO.getToken())).thenReturn(candidate);
        ResponseEntity<?> returnedResult = testRestController.saveOneSqlAnswers(sqlCandidateAnswerDTO);
        verify(tokenService).getCandidateByToken(sqlCandidateAnswerDTO.getToken());
        verify(candidateService).saveCandidateOneSqlAnswer(candidate, sqlCandidateAnswerDTO);

        assertThat(returnedResult.getBody()).isEqualTo("One SQL answer was successfully processed.");
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCodeTaskAnswers() throws Exception {
        SaveCodeAnswerDTOFromUI codeTaskAnswers = new SaveCodeAnswerDTOFromUI();
        ResponseEntity<?> returnedResult = testRestController.saveCodeAnswer(codeTaskAnswers);
        verify(candidateService).saveCandidateCodeAnswer(codeTaskAnswers);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(returnedResult.getBody()).isEqualTo("Code task saved successfully");
    }
}
