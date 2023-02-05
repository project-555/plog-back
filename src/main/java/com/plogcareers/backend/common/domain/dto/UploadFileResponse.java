package com.plogcareers.backend.common.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UploadFileResponse {
    @ApiModelProperty(value = "업로드 된 파일의 URL")
    private String uploadedFileURL;
}
