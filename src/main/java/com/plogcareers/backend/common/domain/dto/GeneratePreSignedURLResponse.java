package com.plogcareers.backend.common.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GeneratePreSignedURLResponse {
    private String preSignedURL;
}
