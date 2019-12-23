package com.internship.evaluation.codetask.aws.sdk;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Client {

    private final AmazonS3 awsS3Client;

    public void pushFileToS3(String bucketName, String destFilePath, @NonNull File file) throws SdkClientException {
        if (null == bucketName || bucketName.isEmpty())
            throw new NullPointerException("folder must not be null or empty");
        if (null == destFilePath || destFilePath.isEmpty())
            throw new NullPointerException("destFileName must not be null or empty");

        awsS3Client.putObject(bucketName, destFilePath, file);
    }
}
