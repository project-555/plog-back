package com.plogcareers.backend.ums.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserLoginResponse {
    private Long userId;
    private String nickName;
    private Token token;
}
