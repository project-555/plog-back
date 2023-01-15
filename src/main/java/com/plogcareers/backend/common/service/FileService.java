package com.plogcareers.backend.common.service;

import com.plogcareers.backend.common.component.PlogObjectStorageClient;
import com.plogcareers.backend.common.domain.dto.UploadFileRequest;
import com.plogcareers.backend.common.domain.dto.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FileService {

    private final PlogObjectStorageClient objectStorageClient;

    public UploadFileResponse uploadFile(UploadFileRequest request) throws IOException {
        // 이미지 파일 Base64 Encoded String을 Decode하여 byte array로 변환
        byte[] decodedBytes = Base64.getDecoder().decode(request.getFileBase64());

        // 파일 업로드
        String uploadedFileURL = objectStorageClient.uploadFile(new ByteArrayInputStream(decodedBytes));
        return UploadFileResponse.builder().uploadedFileURL(uploadedFileURL).build();
    }

}
