package com.internship.evaluation.restcontroller;

import com.internship.evaluation.exception.TimeOut;
import com.internship.evaluation.service.TimerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimerRestControllerTest {
    @Mock
    private TimerService timerService;

    @InjectMocks
    private TimerRestController timerRestController;

    @Test
    public void shouldGetLeftTime() throws TimeOut {
        Integer expectedResult = 3600;
        when(timerService.getLeftTime("token")).thenReturn(3600);
        ResponseEntity<Integer> actualResult = timerRestController.getLeftTime("token");
        verify(timerService).getLeftTime("token");
        assertThat(actualResult.getBody()).isEqualTo(expectedResult);
        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldThrowException() throws TimeOut {
        doThrow(TimeOut.class).when(timerService).getLeftTime("token");
        ResponseEntity<Integer> actualResult = timerRestController.getLeftTime("token");
        verify(timerService).getLeftTime("token");
        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
