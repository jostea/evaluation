package com.internship.evaluation.codetask.resolver;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.internship.evaluation.codetask.CompilationException;
import com.internship.evaluation.codetask.ExecFileBuilder;
import com.internship.evaluation.codetask.aws.sdk.S3Client;
import com.internship.evaluation.codetask.aws.sdk.lambda.CodeTaskEvaluateFunctionImpl;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaRequest;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaResponse;
import com.internship.evaluation.codetask.compiler.Compiler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeTaskEvaluationService {

    private static final String PATH_DELIMITER = "/";

    @Value("${aws.sdk.s3.bucket.evaluation.name}")
    private String evaluationBucketName;

    private final ExecFileBuilder executionFileBuilder;

    private final Compiler compiler;

    private final S3Client s3Client;

    private final CodeTaskEvaluateFunctionImpl evaluateFunction;

    public LambdaResponse evaluate(String taskId, String username) throws IOException, CompilationException {
        log.info("Evaluating task - {} for user - {}", taskId, username);
        try {
            File executionFile = executionFileBuilder.buildEvaluationFile(taskId, username);
            File compiledFile = compiler.compile(executionFile);

            String awsFilePath = username + PATH_DELIMITER + compiledFile.getName();

            log.info("Pushing file - {} to S3 for task - {} and user - {}", compiledFile.getName(), taskId, username);
            s3Client.pushFileToS3(evaluationBucketName, awsFilePath, compiledFile);

            log.info("Executing lambda for file - {} for task - {} and user - {}", compiledFile.getName(), taskId, username);
            return evaluateFunction.execute(new LambdaRequest(evaluationBucketName, awsFilePath));
        } catch (SdkClientException e) {
            log.error("Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.", e);
            throw e;
        } catch (LambdaFunctionException e) {
            log.error("Something wrong with aws lambda function. Timeout, syntax or any other error occurred.", e);
            throw e;
        } catch (IOException e) {
            throw new IOException("Building evaluation file error for taskId - " + taskId + " and user - " + username, e);
        }
    }
}
