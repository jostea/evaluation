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
import com.internship.evaluation.model.dto.test.UserCodeTaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.internship.evaluation.codetask.compiler.Compiler.COMPILED_JAVA_FILE_EXT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeTaskEvaluationService {

    private static final String PATH_DELIMITER = "/";

    @Value("${aws.sdk.s3.bucket.evaluation.name}")
    private String evaluationBucketName;

    @Value("${code.task.template.classtestcasename.value}")
    private String classNameForTestCaseValue;

    private final ExecFileBuilder executionFileBuilder;

    private final Compiler compiler;

    private final S3Client s3Client;

    private final CodeTaskEvaluateFunctionImpl evaluateFunction;

    public LambdaResponse evaluate(final UserCodeTaskDTO userCodeTaskDTO) throws IOException, CompilationException {
        log.info("Evaluating task - {} for user - {}", userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId());
        try {
            File executionFile = executionFileBuilder.buildEvaluationFile(userCodeTaskDTO);
            File compiledExecutionFile = compiler.compile(executionFile);
            File compiledTestCaseFile =
                    new File(compiledExecutionFile.getAbsolutePath().replace(COMPILED_JAVA_FILE_EXT, "_" + classNameForTestCaseValue + COMPILED_JAVA_FILE_EXT));

            String awsTestCaseClassPath = userCodeTaskDTO.getCandidateId() + PATH_DELIMITER + compiledTestCaseFile.getName();
            String awsExecutionFilePath = userCodeTaskDTO.getCandidateId() + PATH_DELIMITER + compiledExecutionFile.getName();

            log.info("Pushing file - {} to S3 for task - {} and user - {}", compiledExecutionFile.getName(),
                     userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId());
            s3Client.pushFileToS3(evaluationBucketName, awsExecutionFilePath, compiledExecutionFile);
            s3Client.pushFileToS3(evaluationBucketName, awsTestCaseClassPath, compiledTestCaseFile);

            log.info("Executing lambda for file - {} for task - {} and user - {}", compiledExecutionFile.getName(),
                     userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId());
            return evaluateFunction.execute(new LambdaRequest(evaluationBucketName, awsExecutionFilePath, awsTestCaseClassPath));
        } catch (SdkClientException e) {
            log.error("Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.", e);
            throw e;
        } catch (LambdaFunctionException e) {
            log.error("Something wrong with aws lambda function. Timeout, syntax or any other error occurred.", e);
            throw e;
        } catch (IOException e) {
            throw new IOException("Building evaluation file error for taskId - " +  userCodeTaskDTO.getQuestionId() + " and user - " + userCodeTaskDTO.getCandidateId(), e);
        }
    }
}
