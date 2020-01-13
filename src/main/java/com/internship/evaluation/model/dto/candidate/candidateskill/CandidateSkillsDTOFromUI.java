package com.internship.evaluation.model.dto.candidate.candidateskill;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandidateSkillsDTOFromUI {

    private Long skillId;

    private String level;
}
