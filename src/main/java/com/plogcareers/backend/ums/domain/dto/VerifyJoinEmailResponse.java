package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class VerifyJoinEmailResponse {
    @ApiModelProperty(value = "회원가입 이메일")
    String email;

    @ApiModelProperty(value = "회원가입 이메일 인증 후 회원가입 시 입력해야 할 토큰 값")
    String verifyToken;
}
