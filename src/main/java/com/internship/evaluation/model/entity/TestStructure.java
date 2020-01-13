package com.internship.evaluation.model.entity;

import com.internship.evaluation.model.enums.ComplexityEnum;
import com.internship.evaluation.model.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "test_structure_table")
public class TestStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    private Stream stream;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskTypeEnum taskType;

    @NotNull(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    private ComplexityEnum complexity;

    @NotNull
    @Max(10)
    @Column(name = "nr_questions")
    private Long nrQuestions;

    @Override
    public String toString() {
        return "TestStructure{" +
                "id=" + id +
                ", nrQuestions=" + nrQuestions +
                '}';
    }
}
