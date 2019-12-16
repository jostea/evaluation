package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "answer_option_table")
public class AnswersOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Answer text is required")
    @Column(name = "answer_option_value")
    private String answerOptionValue;

    @Column(name = "is_correct")
    boolean isCorrect;

    @ManyToOne
    private Task task;

    @OneToMany(mappedBy = "answersOption")
    private List<CandidateSingleTask> candidateSingleTaskList;

    @ManyToMany
    @JoinTable(name = "candidate_multi_answers", joinColumns = @JoinColumn(name = "ao_selected_id"),
            inverseJoinColumns = @JoinColumn(name = "candidate_multi_task_id"))
    private List<CandidateMultiTask> candidateMultiTasks;

    @Override
    public String toString() {
        return "AnswersOption{" +
                "answerOptionValue='" + answerOptionValue + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
