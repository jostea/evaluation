package com.internship.evaluation.codetask.aws.sdk.lambda;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaRequest;
import com.internship.evaluation.codetask.aws.sdk.lambda.model.LambdaResponse;

public interface CodeTaskEvaluateFunction {
    @LambdaFunction(functionName = "internship-final-project-execute-code-task")
    LambdaResponse evaluate(LambdaRequest i);
}
