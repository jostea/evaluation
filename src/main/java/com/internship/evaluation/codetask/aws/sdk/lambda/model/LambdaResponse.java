package com.internship.evaluation.codetask.aws.sdk.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LambdaResponse {
    private int statusCode;
    private List<EvaluationResult> body;
    private String error;
}
