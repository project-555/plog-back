package com.plogcareers.backend.common.controller;

import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.domain.dto.UploadFileRequest;
import com.plogcareers.backend.common.domain.dto.UploadFileResponse;
import com.plogcareers.backend.common.service.FileService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Api(tags = "Common Domain")
public class CommonController {
    private final FileService fileService;

    @PostMapping("/upload-file")
    public ResponseEntity<SResponse> uploadFile(@RequestBody UploadFileRequest request) throws IOException {
        UploadFileResponse response = fileService.uploadFile(request);
        return ResponseEntity.ok().body(new SDataResponse<>(response));
    }
}
