package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "correct_code_table")
public class CorrectCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Code Task is required")
    @ManyToOne
    @JoinColumn(name = "code_task_id")
    private CodeTask codeTask;

    @NotBlank(message = "Input (method's parameters) is required")
    private String input;

    @NotBlank(message = "Output (method's return value) is required")
    private String output;
}
