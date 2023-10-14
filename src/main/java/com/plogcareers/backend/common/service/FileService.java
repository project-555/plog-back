package com.plogcareers.backend.common.service;

import com.plogcareers.backend.common.component.AWSS3Client;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLRequest;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AWSS3Client awsS3Client;

    public GeneratePreSignedURLResponse generatePreSignedURL(GeneratePreSignedURLRequest request) {
        // Pre-Signed URL 생성
        String preSignedURL = awsS3Client.generatePUTPresignedURL(request.getFileName(), request.getContentType());

        return GeneratePreSignedURLResponse.builder()
                .preSignedURL(preSignedURL)
                .build();
    }
}
