package com.plogcareers.backend.common.domain.dto;

import lombok.Getter;

// 커서 기반 페이징 Response DTO
@Getter
public class SCPagingResponse<T> extends SDataResponse<T>{
    private Long lastCursorId;

    public SCPagingResponse(T data, Long lastCursorId) {
        super(data);
        this.lastCursorId = lastCursorId;
    }
}
