package com.internship.evaluation.model.dto.candidate.candidateskill;

import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.CandidateSkill;
import com.internship.evaluation.model.entity.Skill;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandidateSkillDTO {
    private Long id;

    private String level;

    private Long skillId;

    public CandidateSkillDTO(CandidateSkill candidateSkill) {
        this.id = candidateSkill.getId();
        this.skillId = candidateSkill.getSkill().getId();
        this.level = candidateSkill.getLevel();
    }
}
