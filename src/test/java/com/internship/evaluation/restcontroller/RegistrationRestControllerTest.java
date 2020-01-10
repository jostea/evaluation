package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.NotificationService;
import com.internship.evaluation.service.TestTokenService;
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
public class RegistrationRestControllerTest {

    @Mock
    private CandidateService candidateService;

    @Mock
    private TestTokenService testTokenService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RegistrationRestController registrationRestController;

    @Test
    public void shouldRegisterCandidate() {
        CandidateRegistrationDTO candidateRegistrationDTO = new CandidateRegistrationDTO();
        when(candidateService.addCandidate(candidateRegistrationDTO)).thenReturn(1L);
        ResponseEntity<String> responseEntity = registrationRestController.saveTask(candidateRegistrationDTO);
        verify(candidateService).addCandidate(candidateRegistrationDTO);
        verify(testTokenService).generateNewTestToken(1L);
        verify(notificationService).sendTestInvite(1L);
        assertThat(responseEntity.getBody()).isEqualTo("Candidate was registered.");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldNotRegisterCandidate() {
        CandidateRegistrationDTO candidateRegistrationDTO = new CandidateRegistrationDTO();
        when(candidateService.addCandidate(candidateRegistrationDTO)).thenReturn(0L);
        ResponseEntity<String> responseEntity = registrationRestController.saveTask(candidateRegistrationDTO);
        verify(candidateService).addCandidate(candidateRegistrationDTO);
        assertThat(responseEntity.getBody()).isEqualTo("Such candidate is already registered for the selected stream and internship");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
