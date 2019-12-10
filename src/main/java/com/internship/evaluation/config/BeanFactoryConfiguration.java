package com.internship.evaluation.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.internship.evaluation.codetask.aws.sdk.AWSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanFactoryConfiguration {

    private final AWSClient awsClient;

    @Bean
    public AmazonS3 amazonS3EntityFactory() {
        return AmazonS3Client.builder().withRegion(Regions.fromName(awsClient.getDefaultRegion()))
                             .withCredentials(new AWSStaticCredentialsProvider(awsClient.getBasicCredentials())).build();
    }

    @Bean
    public AWSLambda amazonLambdaEntityFactory() {
        return AWSLambdaClient.builder().withRegion(Regions.fromName(awsClient.getDefaultRegion()))
                              .withCredentials(new AWSStaticCredentialsProvider(awsClient.getBasicCredentials())).build();
    }

    public static <T> T lambdaInvokerEntityFactory(AWSLambda awsLambda, Class<T> clazz) {
        return LambdaInvokerFactory.builder().lambdaClient(awsLambda).build(clazz);
    }
}
