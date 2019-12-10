package com.internship.evaluation.model.dto.skill;

import com.internship.evaluation.model.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillsSpecifiedByStreamDTO {
    private Long id;

    private String name;

    private String typeStr;

    public SkillsSpecifiedByStreamDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.setTypeStr(skill.getSkillType().getType());
    }
}
