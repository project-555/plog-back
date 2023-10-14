package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyJoinEmailRequest {
    @Email
    @NotNull
    @ApiModelProperty(value = "회원가입 할 이메일")
    String email;

    @NotNull
    @ApiModelProperty(value = "회원가입 이메일 인증 후 받은 인증 코드")
    String verifyCode;
}
