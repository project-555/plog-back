package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    @NotNull
    @Email
    @ApiParam(value = "비밀번호를 변경하고자 하는 이메일 주소")
    String email;
    @NotNull
    @ApiParam(value = "변경할 비밀번호")
    String password;
    @NotNull
    @ApiParam(value = "비밀번호 변경 이메일 인증 후 받은 인증 토큰")
    String verifyToken;
}
