package com.leo.estoque_api.config.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${storage.secret.key}")
    private String secretKey;

    @Value("${storage.access.key}")
    private String accessKey;

    @Value("${storage.region}")
    private String region;

    @Bean
    AmazonS3 amazonS3() {
        BasicAWSCredentials basicCredentials =
                new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
                .withRegion(region)
                .build();
    }

}
