package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyFindPasswordEmailRequest {
    @ApiModelProperty(value = "비밀번호를 찾고자 하는 이메일 주소")
    private String email;

    @ApiModelProperty(value = "비밀번호 찾기 이메일 인증 후 받은 인증 코드")
    private String verifyCode;
}
