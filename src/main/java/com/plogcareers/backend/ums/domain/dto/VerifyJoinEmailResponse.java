package com.plogcareers.backend.ums.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerifyJoinEmailResponse {
    String email;
    String verifyToken;
}
