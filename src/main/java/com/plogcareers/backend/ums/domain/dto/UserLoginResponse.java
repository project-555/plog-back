package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class UserLoginResponse {
    @ApiModelProperty(value = "엑세스 토큰")
    private Token token;
}
