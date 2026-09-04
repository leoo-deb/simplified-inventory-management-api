package com.leo.estoque_api.config.amazons3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BucketS3 {

    @Value("${aws.service.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.service.s3.region}")
    private String region;

    @Value("${aws.service.s3.bucket.directory}")
    private String bucketDirectory;

    @Value("${aws.service.s3.secret.key}")
    private String secretKey;

    @Value("${aws.service.s3.access.key}")
    private String accessKey;

}
