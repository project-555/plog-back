package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SendVerifyJoinEmailRequest {
    @Email
    @NotNull
    @ApiModelProperty(value = "회원가입 이메일 인증을 보낼 이메일 주소")
    private String email;
}
