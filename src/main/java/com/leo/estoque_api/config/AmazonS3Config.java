package com.leo.estoque_api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.secret.key}")
    private String secret;

    @Value("${aws.access.key}")
    private String key;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials basicCredentials = new BasicAWSCredentials(key, secret);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
                .withRegion(region)
                .build();
    }


}
