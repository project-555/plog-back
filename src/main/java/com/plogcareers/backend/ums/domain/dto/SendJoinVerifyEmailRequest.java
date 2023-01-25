package com.plogcareers.backend.ums.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SendJoinVerifyEmailRequest {
    @Email
    @NotNull
    private String email;
}
