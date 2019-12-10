package com.internship.evaluation.service;

import com.internship.evaluation.exception.CandidateNotFound;
import com.internship.evaluation.exception.StreamNotFound;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillsDTOFromUI;
import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.evaluation.model.dto.stream.StreamDTO;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.SoftSkillType;
import com.internship.evaluation.model.enums.TechnicalSkillType;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.model.enums.ToolSkillType;
import com.internship.evaluation.repository.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateSkillService {

    private final StreamRepository streamRepository;

    private final SkillsRepository skillsRepository;

    private final TestTokenRepository testTokenRepository;

    private final CandidateSkillRepository candidateSkillRepository;

    public List<SkillsSpecifiedByStreamDTO> getSkillForSpecifiedStream(String token) throws StreamNotFound {
        StreamDTO streamDTO = getCandidatesStream(getCandidateByToken(token));
        Optional<Stream> optionalStream = streamRepository.findById(streamDTO.getId());
        List<SkillsSpecifiedByStreamDTO> skillDTOS = new ArrayList<>();
        if (optionalStream.isPresent()) {
            Stream stream = optionalStream.get();
            for (Skill val : stream.getSkill()) {
                skillDTOS.add(new SkillsSpecifiedByStreamDTO(val));
            }
        }
        return skillDTOS;
    }

    private StreamDTO getCandidatesStream(Candidate candidate) throws StreamNotFound {
        Optional<Stream> optionalStream = streamRepository.findById(candidate.getStream().getId());
        if (optionalStream.isPresent())
            return new StreamDTO(optionalStream.get());
        else throw new StreamNotFound(candidate.getStream().getId() + "");
    }

    private Candidate getCandidateByToken(String token) {
        Candidate candidate = null;
        Optional<TestToken> entityOpt = testTokenRepository.findFirstByToken(token);
        if (entityOpt.isPresent()) {
            candidate = entityOpt.get().getCandidate();
        }
        return candidate;
    }

    public List<List<SkillDTO>> getSkillSpecifiedBySkillType(String token) throws StreamNotFound {
        List<SkillsSpecifiedByStreamDTO> skills = getSkillForSpecifiedStream(token);
        List<SkillDTO> skillsWithTypeTool = new ArrayList<>();
        List<SkillDTO> skillsWithTypeSoft = new ArrayList<>();
        List<SkillDTO> skillsWithTechnical = new ArrayList<>();
        for (SkillsSpecifiedByStreamDTO s : skills) {
            if (s.getTypeStr().equalsIgnoreCase("Tool")) {
                Optional<Skill> skill = skillsRepository.findById(s.getId());
                skill.ifPresent(value -> skillsWithTypeTool.add(new SkillDTO(value)));
            }
            if (s.getTypeStr().equalsIgnoreCase("Technical")) {
                Optional<Skill> skill = skillsRepository.findById(s.getId());
                skill.ifPresent(value -> skillsWithTechnical.add(new SkillDTO(value)));
            }
            if (s.getTypeStr().equalsIgnoreCase("Soft")) {
                Optional<Skill> skill = skillsRepository.findById(s.getId());
                skill.ifPresent(value -> skillsWithTypeSoft.add(new SkillDTO(value)));
            }
        }
        List<List<SkillDTO>> sortedListByType = new ArrayList<>();
        sortedListByType.add(skillsWithTypeTool);
        sortedListByType.add(skillsWithTypeSoft);
        sortedListByType.add(skillsWithTechnical);
        return sortedListByType;
    }

    public void populateCandidatesSkills(String token) throws StreamNotFound {
        Candidate candidate = getCandidateByToken(token);
        if (candidate.getTestStatus().equals(TestStatusEnum.WAITING_ACTIVATION)) {
            Optional<Stream> optionalStream = streamRepository.findById(candidate.getStream().getId());
            if (optionalStream.isPresent()) {
                Stream stream = optionalStream.get();
                for (Skill val : stream.getSkill()) {
                    candidateSkillRepository.save(
                            CandidateSkill.builder()
                                    .skill(val)
                                    .candidate(candidate)
                                    .build());
                }
            } else throw new StreamNotFound("candidate id" + candidate.getId() + "");
        }
    }

    public void updateCandidateSkills(List<CandidateSkillsDTOFromUI> candidateFromUI) {
        for (CandidateSkillsDTOFromUI candidateUI : candidateFromUI) {
            Optional<CandidateSkill> candidateSkillOptional = candidateSkillRepository.findCandidateSkillBySkillId(candidateUI.getSkillId());
            if (candidateSkillOptional.isPresent()) {
                CandidateSkill candidateSkill = candidateSkillOptional.get();

                for (int i = 0; i < 5; i++) {
                    if (TechnicalSkillType.values()[i].getType().equalsIgnoreCase(candidateUI.getLevel()) ||
                            ToolSkillType.values()[i].getType().equalsIgnoreCase(candidateUI.getLevel()) ||
                            SoftSkillType.values()[i].getType().equalsIgnoreCase(candidateUI.getLevel())) {
                        candidateSkill.setLevel(candidateUI.getLevel());
                        break;
                    }
                }
                candidateSkillRepository.save(candidateSkill);
            }
        }
    }
}
