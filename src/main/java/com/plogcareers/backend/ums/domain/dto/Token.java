package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Token {
    @ApiModelProperty(value = "로그인 엑세스 토큰")
    private String accessToken;
}
