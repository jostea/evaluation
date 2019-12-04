package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "candidate_skill_table")
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Candidate is required")
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @NotNull(message = "Skill is required")
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @NotNull(message = "Level is required")
    private String level;

}
