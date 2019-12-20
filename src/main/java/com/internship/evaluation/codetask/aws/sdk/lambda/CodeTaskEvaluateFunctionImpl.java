package com.internship.evaluation.codetask.aws.sdk.lambda;

import com.amazonaws.services.lambda.AWSLambda;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaRequest;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.internship.evaluation.config.BeanFactoryConfiguration.lambdaInvokerEntityFactory;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeTaskEvaluateFunctionImpl {

    private final AWSLambda awsLambdaClient;

    public LambdaResponse execute(LambdaRequest req) {
        CodeTaskEvaluateFunction e = lambdaInvokerEntityFactory(awsLambdaClient, CodeTaskEvaluateFunction.class);
        return e.evaluate(req);
    }
}
