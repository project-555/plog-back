package com.plogcareers.backend.common.domain.dto;

import lombok.Getter;

@Getter
public class SOPagingResponse<T> extends SDataResponse<T> implements SResponse{
    int page;
    int rowsPerPage;
    Long tCnt;

    public SOPagingResponse(T data, int page, int rowsPerPage, Long tCnt) {
        super(data);
        this.page = page;
        this.rowsPerPage = rowsPerPage;
        this.tCnt = tCnt;
    }
}
