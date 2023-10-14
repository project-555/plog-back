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
public class ChangePasswordRequest {
    @NotNull
    @Email
    @ApiModelProperty(value = "비밀번호를 변경하고자 하는 이메일 주소")
    String email;
    @NotNull
    @ApiModelProperty(value = "변경할 비밀번호")
    String password;
    @NotNull
    @ApiModelProperty(value = "비밀번호 변경 이메일 인증 후 받은 인증 토큰")
    String verifyToken;
}
