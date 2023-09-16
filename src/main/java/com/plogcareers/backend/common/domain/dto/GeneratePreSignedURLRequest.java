package com.plogcareers.backend.common.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GeneratePreSignedURLRequest {
    @NotBlank
    private String fileName;
    @NotBlank
    private String contentType;
}
