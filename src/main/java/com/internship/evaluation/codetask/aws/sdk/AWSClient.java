package com.internship.evaluation.codetask.aws.sdk;

import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AWSClient {

    private String accessKey;

    private String secretKey;

    private String defaultRegion;

    private BasicAWSCredentials basicCredentials;

    public AWSClient(@Value("${aws.sdk.access.key}") String accessKey,
                     @Value("${aws.sdk.secret.key}") String secretKey,
                     @Value("${aws.region.default}") String defaultRegion) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.defaultRegion = defaultRegion;
        this.basicCredentials = new BasicAWSCredentials(accessKey, secretKey);
    }
}
