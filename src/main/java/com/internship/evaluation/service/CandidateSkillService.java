package com.internship.evaluation.service;

import com.internship.evaluation.exception.StreamNotFound;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillDTO;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillsDTOFromUI;
import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.evaluation.model.dto.stream.StreamDTO;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.*;
import com.internship.evaluation.repository.*;
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

    public List<SkillsSpecifiedByStreamDTO> getSkillForSpecifiedStream(String token) throws StreamNotFound, Exception {
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

    public List<List<SkillDTO>> getSkillSpecifiedBySkillType(String token) throws StreamNotFound, Exception {
        List<SkillsSpecifiedByStreamDTO> skills = getSkillForSpecifiedStream(token);
        List<SkillDTO> skillsWithTypeTool = new ArrayList<>();
        List<SkillDTO> skillsWithTypeSoft = new ArrayList<>();
        List<SkillDTO> skillsWithTechnical = new ArrayList<>();
        List<SkillDTO> skillsWithLanguage = new ArrayList<>();
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
            if (s.getTypeStr().equalsIgnoreCase("Language")) {
                Optional<Skill> skill = skillsRepository.findById(s.getId());
                skill.ifPresent(value -> skillsWithLanguage.add(new SkillDTO(value)));
            }
        }
        List<List<SkillDTO>> sortedListByType = new ArrayList<>();
        sortedListByType.add(skillsWithTypeTool);
        sortedListByType.add(skillsWithTypeSoft);
        sortedListByType.add(skillsWithTechnical);
        sortedListByType.add(skillsWithLanguage);
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

    public void updateCandidateSkills(List<CandidateSkillsDTOFromUI> candidateFromUI, String token) {
        for (CandidateSkillsDTOFromUI candidateUI : candidateFromUI) {
            Optional<CandidateSkill> candidateSkillOptional = candidateSkillRepository.findCandidateSkillBySkillIdAndCandidateId(candidateUI.getSkillId(), getCandidateByToken(token).getId());
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

                for (LanguageSkillType eng : LanguageSkillType.values()) {
                    if (eng.getType().equalsIgnoreCase(candidateUI.getLevel())) {
                        candidateSkill.setLevel(candidateUI.getLevel());
                        break;
                    }
                }

                candidateSkillRepository.save(candidateSkill);
            }
        }
    }

    public List<CandidateSkillDTO> findAllCandidatesSkills(String token) throws Exception {
        List<CandidateSkillDTO> candidateSkillDTOS = new ArrayList<>();
        for (CandidateSkill candidateSkill : candidateSkillRepository.findAllByCandidateId(getCandidateByToken(token).getId())) {
            candidateSkillDTOS.add(new CandidateSkillDTO(candidateSkill));
        }
        return candidateSkillDTOS;
    }
}
