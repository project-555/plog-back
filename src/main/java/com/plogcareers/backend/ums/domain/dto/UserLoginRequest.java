package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @ApiModelProperty(value = "로그인 이메일")
    private String email;

    @ApiModelProperty(value = "로그인 비밀번호")
    private String password;
}
