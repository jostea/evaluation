package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate_custom_task")
public class CandidateCustomTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "custom_task_id")
    private Task task;

//    @JoinColumn(name = "custom_answer")
    @Column(name = "custom_answer")
    private String customAnswer;

    @Column(name = "is_correct")
    private boolean isCorrect;

    public CandidateCustomTask(Task task, Candidate candidate) {
        this.task = task;
        this.candidate = candidate;
    }
}
