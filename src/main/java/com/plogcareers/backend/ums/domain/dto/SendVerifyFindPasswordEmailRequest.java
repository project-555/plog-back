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
public class SendVerifyFindPasswordEmailRequest {
    @NotNull
    @Email
    @ApiModelProperty(value = "비밀번호를 찾고자 하는 이메일 주소")
    String email;
}
