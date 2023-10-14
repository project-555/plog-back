package com.plogcareers.backend.common.component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AWSS3Client {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String generatePUTPresignedURL(String fileName, String contentType) {
        String objectKey = UUID.randomUUID() + "/" + fileName;

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.PUT)
                .withContentType(contentType)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)); // 10분간 유효한 URL 생성

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
