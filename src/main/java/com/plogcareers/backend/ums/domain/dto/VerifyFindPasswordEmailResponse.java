package com.plogcareers.backend.ums.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyFindPasswordEmailResponse {
    private String email;
    private String verifyToken;
}
