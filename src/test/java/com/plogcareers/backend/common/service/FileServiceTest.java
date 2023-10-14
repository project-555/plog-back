package com.plogcareers.backend.common.service;

import com.plogcareers.backend.common.component.AWSS3Client;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLRequest;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    AWSS3Client awsS3Client;

    @InjectMocks
    FileService fileService;

    @Test
    @DisplayName("generatePreSignedURL - 정상동작")
    void generatePreSignedURL() {
        // given
        when(
                awsS3Client.generatePUTPresignedURL("fileName", "contentType")
        ).thenReturn(
                "preSignedURL"
        );
        // when
        GeneratePreSignedURLResponse got = fileService.generatePreSignedURL(
                GeneratePreSignedURLRequest.builder()
                        .contentType("contentType")
                        .fileName("fileName")
                        .build());
        // then
        GeneratePreSignedURLResponse want = GeneratePreSignedURLResponse.builder()
                .preSignedURL("preSignedURL")
                .build();

        Assertions.assertEquals(want, got);
    }
}