package com.plogcareers.backend.common.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UploadFileRequest {
    @ApiModelProperty(value = "업로드 할 파일 byte를 base64로 인코딩한 값")
    String fileBase64;
}
