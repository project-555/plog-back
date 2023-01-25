package com.plogcareers.backend.ums.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyFindPasswordEmailRequest {
    private String email;
    private String verifyCode;
}
