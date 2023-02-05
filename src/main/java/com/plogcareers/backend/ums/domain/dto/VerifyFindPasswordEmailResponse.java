package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyFindPasswordEmailResponse {
    @ApiModelProperty(value = "비밀번호 찾고자 하는 이메일")
    private String email;

    @ApiModelProperty(value = "비밀번호 변경 시 입력해야 할 토큰 값")
    private String verifyToken;
}
