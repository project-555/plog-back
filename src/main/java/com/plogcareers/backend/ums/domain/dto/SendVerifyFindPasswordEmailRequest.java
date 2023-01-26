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
public class SendVerifyFindPasswordEmailRequest {
    @NotNull
    @Email
    @ApiParam(value = "비밀번호를 찾고자 하는 이메일 주소")
    String email;
}
