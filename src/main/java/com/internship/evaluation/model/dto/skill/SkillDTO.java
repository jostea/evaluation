package com.internship.evaluation.model.dto.skill;

import com.internship.evaluation.model.dto.stream.StreamDTO;
import com.internship.evaluation.model.entity.Skill;
import com.internship.evaluation.model.entity.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {
    private Long id;

    private String name;

    private String typeStr;

    private List<StreamDTO> streams;

    public SkillDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
            streams = new ArrayList<>();
        for (Stream s : skill.getStreams()) {
            this.streams.add(new StreamDTO(s));
        }
        this.setTypeStr(skill.getSkillType().getType());
    }
}