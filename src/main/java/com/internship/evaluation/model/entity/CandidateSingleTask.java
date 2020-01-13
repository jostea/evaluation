package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate_single_task")
public class CandidateSingleTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "singlechoice_task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "ao_selected_id")
    private AnswersOption answersOption;

    public CandidateSingleTask(Task task, Candidate candidate) {
        this.task = task;
        this.candidate = candidate;

    }

}
