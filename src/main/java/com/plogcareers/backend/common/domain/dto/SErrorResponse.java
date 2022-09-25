package com.plogcareers.backend.common.domain.dto;

import lombok.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class SErrorResponse implements SResponse{
    private int code;
    private String message;
}
