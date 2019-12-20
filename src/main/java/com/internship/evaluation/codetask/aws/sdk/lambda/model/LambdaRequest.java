package com.internship.evaluation.codetask.aws.sdk.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LambdaRequest {
    private String bucketName;
    private String mainFilePath;
    private String testCaseFilePath;
}
