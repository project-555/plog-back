package com.plogcareers.backend.common.controller;

import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLRequest;
import com.plogcareers.backend.common.domain.dto.GeneratePreSignedURLResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Api(tags = "Common Domain")
public class CommonController {
    private final FileService fileService;

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
