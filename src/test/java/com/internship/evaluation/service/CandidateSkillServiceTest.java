package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.CandidateSkill;
import com.internship.evaluation.model.entity.Skill;
import com.internship.evaluation.model.entity.TestToken;
import com.internship.evaluation.model.enums.SkillsTypeEnum;
import com.internship.evaluation.repository.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CandidateSkillServiceTest {

    @Mock
    private StreamRepository streamRepository;

    @Mock
    private SkillsRepository skillsRepository;

    @Mock
    private TestTokenRepository testTokenRepository;

    @Mock
    private CandidateSkillRepository candidateSkillRepository;

    @InjectMocks
    private CandidateSkillService candidateSkillService;

    @Rule
    private ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFindAllCandidatesSkills() {
        Candidate candidate = Candidate.builder().id(1L).build();
        TestToken testToken = TestToken.builder().candidate(candidate).token("token").build();
        List<CandidateSkillDTO> expectedList = new ArrayList<>();
        expectedList.add(new CandidateSkillDTO(
                CandidateSkill.builder()
                        .id(1L)
                        .candidate(candidate)
                        .skill(Skill.builder().id(1L).skillType(SkillsTypeEnum.TOOL).build())
                        .level("Never Used")
                        .build()));
        List<CandidateSkill> mockedList = new ArrayList<>();
        mockedList.add(CandidateSkill.builder()
                .id(1L)
                .candidate(candidate)
                .skill(Skill.builder().id(1L).skillType(SkillsTypeEnum.TOOL).build())
                .level("Never Used")
                .build());

        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(candidateSkillRepository.findAllByCandidateId(candidate.getId())).thenReturn(mockedList);

        List<CandidateSkillDTO> returnedList = candidateSkillService.findAllCandidatesSkills("token");

        verify(testTokenRepository).findFirstByToken("token");
        verify(candidateSkillRepository).findAllByCandidateId(candidate.getId());
        assertThat(expectedList).isEqualTo(returnedList);
    }
}
