package com.internship.evaluation.codetask.aws.sdk.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResult {
    private int questionId;
    private int caseId;
    private String result;
}
