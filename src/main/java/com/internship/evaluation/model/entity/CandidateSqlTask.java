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
@Table(name = "candidate_sql_task")
public class CandidateSqlTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "sql_task_id")
    private SqlTask sqlTask;

    @Column(name = "sql_statement_provided")
    private String statementProvided;

    @Column(name = "is_correct")
    private boolean isCorrect;

}
