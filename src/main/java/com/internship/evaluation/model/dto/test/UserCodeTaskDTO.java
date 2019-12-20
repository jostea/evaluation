package com.internship.evaluation.model.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCodeTaskDTO {
    private String questionId;
    private String candidateId;
    private String signature;
    private String userAnswer;
    private List<TestCaseDTO> testCases;
}
