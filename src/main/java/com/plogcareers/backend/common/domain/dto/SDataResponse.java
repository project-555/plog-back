package com.plogcareers.backend.common.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SDataResponse<T> implements SResponse{
    private T data;
}

