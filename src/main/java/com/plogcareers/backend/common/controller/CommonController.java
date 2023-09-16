package com.plogcareers.backend.common.controller;

import com.plogcareers.backend.common.domain.dto.*;
import com.plogcareers.backend.common.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "파일 업로드", notes = "파일을 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "파일 업로드 성공", response = UploadFileResponse.class),
            @ApiResponse(code = 400, message = "파일 업로드 실패", response = ErrorResponse.class)
    })
    @PostMapping("/upload-file")
    public ResponseEntity<SResponse> uploadFile(@RequestBody UploadFileRequest request) throws IOException {
        UploadFileResponse response = fileService.uploadFile(request);
        return ResponseEntity.ok().body(new SDataResponse<>(response));
    }

    @ApiOperation(value = "AWS Pre-Signed URL 생성", notes = "Pre-Signed URL을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pre-Signed URL 생성 성공", response = GeneratePreSignedURLResponse.class),
            @ApiResponse(code = 400, message = "Pre-Signed URL 생성 실패", response = ErrorResponse.class)
    })
    @PostMapping("/generate-presigned-url")
    public ResponseEntity<GeneratePreSignedURLResponse> generatePreSignedURL(@RequestBody GeneratePreSignedURLRequest request) {
        return ResponseEntity.ok().body(fileService.generatePreSignedURL(request));
    }
}
