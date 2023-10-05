package com.plogcareers.backend.common.service;

import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLRequest;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Service s3Service;

    public GeneratePreSignedURLResponse generatePreSignedURL(GeneratePreSignedURLRequest request) {
        // Pre-Signed URL 생성
        String preSignedURL = s3Service.generatePUTPresignedURL(request.getFileName(), request.getContentType());

        return GeneratePreSignedURLResponse.builder()
                .preSignedURL(preSignedURL)
                .build();
    }
}
