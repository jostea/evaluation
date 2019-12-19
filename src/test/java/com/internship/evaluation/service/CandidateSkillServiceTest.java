package com.internship.evaluation.service;

import com.internship.evaluation.exception.StreamNotFound;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillDTO;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillsDTOFromUI;
import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.SkillsTypeEnum;
import com.internship.evaluation.model.enums.TestStatusEnum;
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
import static org.mockito.Mockito.*;

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
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFindAllCandidatesSkills() throws Exception {
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

    @Test
    public void shouldPopulateCandidatesSkills() throws StreamNotFound {
        List<Skill> skills = new ArrayList<>();
        skills.add(Skill.builder()
                .id(1L)
                .skillType(SkillsTypeEnum.TOOL)
                .name("Spring Boot")
                .build());
        Stream stream = Stream.builder()
                .id(1L)
                .skill(skills)
                .build();
        Candidate candidate = Candidate.builder().id(1L)
                .stream(Stream.builder()
                        .id(1L)
                        .build())
                .testStatus(TestStatusEnum.WAITING_ACTIVATION)
                .build();
        TestToken testToken = TestToken.builder().candidate(candidate).token("token").build();
        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(streamRepository.findById(candidate.getStream().getId())).thenReturn(Optional.of(stream));
        candidateSkillService.populateCandidatesSkills("token");
        verify(testTokenRepository).findFirstByToken("token");
        verify(streamRepository).findById(1L);
    }

    @Test
    public void shouldGetSkillForSpecifiedStream() throws StreamNotFound, Exception {
        List<SkillsSpecifiedByStreamDTO> skillDTOS = new ArrayList<>();

        skillDTOS.add(new SkillsSpecifiedByStreamDTO(Skill.builder()
                .id(1L)
                .name("Spring")
                .skillType(SkillsTypeEnum.TOOL)
                .build()));

        List<Skill> skills = new ArrayList<>();

        skills.add(Skill.builder()
                .id(1L)
                .name("Spring")
                .skillType(SkillsTypeEnum.TOOL)
                .build());

        Stream stream = Stream.builder()
                .id(1L)
                .name("TestStream")
                .skill(skills)
                .discipline(Discipline.builder()
                        .id(1L)
                        .name("TestDiscipline")
                        .build())
                .build();

        Candidate candidate = Candidate.builder()
                .id(1L)
                .stream(stream)
                .build();
        TestToken testToken = TestToken.builder()
                .candidate(candidate)
                .build();

        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(streamRepository.findById(1L)).thenReturn(Optional.of(stream));

        List<SkillsSpecifiedByStreamDTO> returnedList = candidateSkillService.getSkillForSpecifiedStream("token");
        verify(testTokenRepository).findFirstByToken("token");
        verify(streamRepository, times(2)).findById(1L);

        assertThat(returnedList).isEqualTo(skillDTOS);
    }

    @Test
    public void shouldGetSkillSpecifiedBySkillType() throws StreamNotFound, Exception {
        List<SkillDTO> skillsWithTypeTool = new ArrayList<>();
        List<SkillDTO> skillsWithTypeSoft = new ArrayList<>();
        List<SkillDTO> skillsWithTechnical = new ArrayList<>();
        List<SkillDTO> skillsWithLanguage = new ArrayList<>();
        List<Stream> streams = new ArrayList<>();
        Stream stream = Stream.builder()
                .id(1L)
                .name("TestStream")
                .discipline(Discipline.builder()
                        .id(1L)
                        .name("TestDiscipline")
                        .build())
                .build();
        streams.add(stream);

        List<SkillsSpecifiedByStreamDTO> skillDTOS = new ArrayList<>();
        Skill skill1 = Skill.builder()
                .id(1L)
                .name("Selenium")
                .streams(streams)
                .skillType(SkillsTypeEnum.TOOL)
                .build();

        Skill skill2 = Skill.builder()
                .id(2L)
                .name("Java SE")
                .streams(streams)
                .skillType(SkillsTypeEnum.TECHNICAL)
                .build();
        Skill skill3 = Skill.builder()
                .id(3L)
                .name("Spring")
                .streams(streams)
                .skillType(SkillsTypeEnum.SOFT)
                .build();
        Skill skill4 = Skill.builder()
                .id(4L)
                .name("English")
                .streams(streams)
                .skillType(SkillsTypeEnum.LANGUAGE)
                .build();
        List<Skill> skills = new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        skills.add(skill4);
        stream.setSkill(skills);
        skillDTOS.add(new SkillsSpecifiedByStreamDTO(skill1));
        skillDTOS.add(new SkillsSpecifiedByStreamDTO(skill2));
        skillDTOS.add(new SkillsSpecifiedByStreamDTO(skill3));
        skillDTOS.add(new SkillsSpecifiedByStreamDTO(skill4));

        skillsWithTypeTool.add(new SkillDTO(skill1));
        skillsWithTypeSoft.add(new SkillDTO(skill2));
        skillsWithTechnical.add(new SkillDTO(skill3));
        skillsWithLanguage.add(new SkillDTO(skill4));
        List<List<SkillDTO>> expectedList = new ArrayList<>();
        expectedList.add(skillsWithTypeTool);
        expectedList.add(skillsWithTechnical);
        expectedList.add(skillsWithTypeSoft);
        expectedList.add(skillsWithLanguage);
        Candidate candidate = Candidate.builder()
                .id(1L)
                .stream(stream)
                .build();
        TestToken testToken = TestToken.builder()
                .candidate(candidate)
                .build();
        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(streamRepository.findById(1L)).thenReturn(Optional.of(stream));
        when(skillsRepository.findById(1L)).thenReturn(Optional.of(skill1));
        when(skillsRepository.findById(2L)).thenReturn(Optional.of(skill2));
        when(skillsRepository.findById(3L)).thenReturn(Optional.of(skill3));
        when(skillsRepository.findById(4L)).thenReturn(Optional.of(skill4));

        List<List<SkillDTO>> returnedSkills = candidateSkillService.getSkillSpecifiedBySkillType("token");

        verify(skillsRepository).findById(1L);
        verify(skillsRepository).findById(2L);
        verify(skillsRepository).findById(3L);
        verify(skillsRepository).findById(4L);
        assertThat(returnedSkills).isEqualTo(expectedList);
    }

    @Test
    public void shouldUpdateCandidateSkills() {
        List<CandidateSkillsDTOFromUI> candidateFromUI = new ArrayList<>();
        candidateFromUI.add(CandidateSkillsDTOFromUI.builder().skillId(1L).level("Never Used").build());
        candidateFromUI.add(CandidateSkillsDTOFromUI.builder().skillId(2L).level("Sometimes").build());
        candidateFromUI.add(CandidateSkillsDTOFromUI.builder().skillId(3L).level("Never Used").build());
        candidateFromUI.add(CandidateSkillsDTOFromUI.builder().skillId(4L).level("B1").build());

        Candidate candidate = Candidate.builder()
                .id(1L)
                .build();
        TestToken testToken = TestToken.builder()
                .candidate(candidate)
                .build();

        Skill skill1 = Skill.builder()
                .id(1L)
                .name("Selenium")
                .skillType(SkillsTypeEnum.TOOL)
                .build();

        Skill skill2 = Skill.builder()
                .id(2L)
                .name("Java SE")
                .skillType(SkillsTypeEnum.TECHNICAL)
                .build();
        Skill skill3 = Skill.builder()
                .id(3L)
                .name("Spring")
                .skillType(SkillsTypeEnum.SOFT)
                .build();
        Skill skill4 = Skill.builder()
                .id(4L)
                .name("English")
                .skillType(SkillsTypeEnum.LANGUAGE)
                .build();

        CandidateSkill candidateSkill1 = CandidateSkill.builder()
                .candidate(Candidate.builder().id(1L).build())
                .skill(skill1).build();
        CandidateSkill candidateSkill2 = CandidateSkill.builder()
                .candidate(Candidate.builder().id(1L).build())
                .skill(skill2).build();

        CandidateSkill candidateSkill3 = CandidateSkill.builder()
                .candidate(Candidate.builder().id(1L).build())
                .skill(skill3).build();

        CandidateSkill candidateSkill4 = CandidateSkill.builder()
                .candidate(Candidate.builder().id(1L).build())
                .skill(skill4).build();

        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(candidateSkillRepository.findCandidateSkillBySkillIdAndCandidateId(1L, 1L)).thenReturn(Optional.of(candidateSkill1));
        when(candidateSkillRepository.findCandidateSkillBySkillIdAndCandidateId(2L, 1L)).thenReturn(Optional.of(candidateSkill2));
        when(candidateSkillRepository.findCandidateSkillBySkillIdAndCandidateId(3L, 1L)).thenReturn(Optional.of(candidateSkill3));
        when(candidateSkillRepository.findCandidateSkillBySkillIdAndCandidateId(4L, 1L)).thenReturn(Optional.of(candidateSkill4));


        candidateSkillService.updateCandidateSkills(candidateFromUI, "token");

        verify(candidateSkillRepository).save(candidateSkill1);
        verify(candidateSkillRepository).save(candidateSkill2);
        verify(candidateSkillRepository).save(candidateSkill3);
        verify(candidateSkillRepository).save(candidateSkill4);
    }

    @Test
    public void shouldTrowExceptionStreamNotFoundWhileGetSkillForSpecifiedStream() throws StreamNotFound, Exception {
        expectedException.expectMessage(1L + "");
        expectedException.expect(StreamNotFound.class);

        Candidate candidate = Candidate.builder()
                .id(1L)
                .stream(Stream.builder().id(1L).build())
                .build();
        TestToken testToken = TestToken.builder()
                .candidate(candidate)
                .build();

        when(testTokenRepository.findFirstByToken("token")).thenReturn(Optional.of(testToken));
        when(streamRepository.findById(1L)).thenReturn(Optional.empty());
        candidateSkillService.getSkillForSpecifiedStream("token");
        verify(testTokenRepository).findFirstByToken("token");
        verify(streamRepository, times(2)).findById(1L);
    }
}
