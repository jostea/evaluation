package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate_multi_task")
public class CandidateMultiTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "multichoice_task_id")
    private Task task;

    @ManyToMany
    @JoinTable(name = "candidate_multi_answers", joinColumns = @JoinColumn(name = "candidate_multi_task_id"),
            inverseJoinColumns = @JoinColumn(name = "ao_selected_id"))
    private List<AnswersOption> answersOptions;
}
