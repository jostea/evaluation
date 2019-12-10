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

    private Long candidate_id;

    private SkillDTO skill;

    private String level;

    public CandidateSkillDTO(CandidateSkill candidateSkill) {
        this.id = candidateSkill.getId();
        this.skill = new SkillDTO(candidateSkill.getSkill());
        this.level = candidateSkill.getLevel();
    }
}
