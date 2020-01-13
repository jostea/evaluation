package com.internship.evaluation.restcontroller;

import com.internship.evaluation.exception.StreamNotFound;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillDTO;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillsDTOFromUI;
import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.evaluation.model.entity.Skill;
import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.enums.SkillsTypeEnum;
import com.internship.evaluation.service.CandidateSkillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidateSkillRestControllerTest {

    @Mock
    private CandidateSkillService candidateSkillService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CandidateSkillRestController candidateSkillRestController;

    @Test
    public void shouldReturnCandidatesSkills() throws StreamNotFound, Exception {
        List<SkillsSpecifiedByStreamDTO> expectedList = new ArrayList<>();
        expectedList.add(SkillsSpecifiedByStreamDTO.builder().name("TestSkill").build());
        List<SkillsSpecifiedByStreamDTO> mockedList = new ArrayList<>();
        mockedList.add(SkillsSpecifiedByStreamDTO.builder().name("TestSkill").build());
        when(candidateSkillService.getSkillForSpecifiedStream("token")).thenReturn(mockedList);
        ResponseEntity<List<SkillsSpecifiedByStreamDTO>> responseEntity = candidateSkillRestController.getCandidatesSkills("token", authentication);
        verify(candidateSkillService).getSkillForSpecifiedStream("token");
        assertThat(responseEntity.getBody()).isEqualTo(expectedList);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void shouldReturnSkillsSpecifiedByType() throws StreamNotFound, Exception {
        List<List<SkillDTO>> expectedResult = new ArrayList<>();
        List<SkillDTO> skillDTOS = new ArrayList<>();
        skillDTOS.add(SkillDTO.builder().name("TestSkill").build());
        expectedResult.add(skillDTOS);
        List<List<SkillDTO>> mockedList = new ArrayList<>();
        List<SkillDTO> mockedSkills = new ArrayList<>();
        mockedSkills.add(SkillDTO.builder().name("TestSkill").build());
        mockedList.add(mockedSkills);
        when(candidateSkillService.getSkillSpecifiedBySkillType("token")).thenReturn(mockedList);
        ResponseEntity<List<List<SkillDTO>>> responseEntity = candidateSkillRestController.getSkillsSpecifiedByType("token", authentication);
        verify(candidateSkillService).getSkillSpecifiedBySkillType("token");
        assertThat(responseEntity.getBody()).isEqualTo(expectedResult);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldSaveCandidatesSkills() {
        List<CandidateSkillsDTOFromUI> list = new ArrayList<>();
        ResponseEntity<String> responseEntity = candidateSkillRestController.saveCandidatesSkills("token", list, authentication);
        verify(candidateSkillService).updateCandidateSkills(list, "token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldPopulateCandidateSkills() throws StreamNotFound {
        ResponseEntity<String> responseEntity = candidateSkillRestController.populateCandidateSkills("token", authentication);
        verify(candidateSkillService).populateCandidatesSkills("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnAllCandidatesSkills() throws Exception {
        List<CandidateSkillDTO> expectedList = new ArrayList<>();
        expectedList.add(CandidateSkillDTO.builder().id(1L).skillId(1L).level("Never Used").build());
        List<CandidateSkillDTO> mockedList = new ArrayList<>();
        mockedList.add(CandidateSkillDTO.builder().id(1L).skillId(1L).level("Never Used").build());
        when(candidateSkillService.findAllCandidatesSkills("token")).thenReturn(mockedList);
        ResponseEntity<List<CandidateSkillDTO>> responseEntity = candidateSkillRestController.getAllCandidatesSkills("token", authentication);
        verify(candidateSkillService).findAllCandidatesSkills("token");
        assertThat(responseEntity.getBody()).isEqualTo(expectedList);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhileGetCandidatesSkills() throws StreamNotFound, Exception {
        doThrow(StreamNotFound.class).when(candidateSkillService).getSkillForSpecifiedStream("token");
        ResponseEntity<List<SkillsSpecifiedByStreamDTO>> responseEntity = candidateSkillRestController.getCandidatesSkills("token", authentication);
        verify(candidateSkillService).getSkillForSpecifiedStream("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionWhileGetCandidatesSkills() throws StreamNotFound, Exception {
        doThrow(Exception.class).when(candidateSkillService).getSkillForSpecifiedStream(null);
        ResponseEntity<List<SkillsSpecifiedByStreamDTO>> responseEntity = candidateSkillRestController.getCandidatesSkills(null, authentication);
        verify(candidateSkillService).getSkillForSpecifiedStream(null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhileGetSkillsSpecifiedByType() throws StreamNotFound, Exception {
        doThrow(StreamNotFound.class).when(candidateSkillService).getSkillSpecifiedBySkillType("token");
        ResponseEntity<List<List<SkillDTO>>> responseEntity = candidateSkillRestController.getSkillsSpecifiedByType("token", authentication);
        verify(candidateSkillService).getSkillSpecifiedBySkillType("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionWhileGetSkillsSpecifiedByType() throws StreamNotFound, Exception {
        doThrow(Exception.class).when(candidateSkillService).getSkillSpecifiedBySkillType("token");
        ResponseEntity<List<List<SkillDTO>>> responseEntity = candidateSkillRestController.getSkillsSpecifiedByType("token", authentication);
        verify(candidateSkillService).getSkillSpecifiedBySkillType("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhilePopulateCandidateSkills() throws StreamNotFound, Exception {
        doThrow(StreamNotFound.class).when(candidateSkillService).populateCandidatesSkills("token");
        ResponseEntity<String> responseEntity = candidateSkillRestController.populateCandidateSkills("token", authentication);
        verify(candidateSkillService).populateCandidatesSkills("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("This candidate didn't have stream");
    }

    @Test
    public void shouldThrowExceptionWhileGetAllCandidatesSkills() throws Exception {
        doThrow(Exception.class).when(candidateSkillService).findAllCandidatesSkills(null);
        ResponseEntity<List<CandidateSkillDTO>> responseEntity = candidateSkillRestController.getAllCandidatesSkills(null, authentication);
        verify(candidateSkillService).findAllCandidatesSkills(null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
