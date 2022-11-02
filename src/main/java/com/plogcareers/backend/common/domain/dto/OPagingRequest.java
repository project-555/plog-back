package com.plogcareers.backend.common.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OPagingRequest {
    @ApiParam(name = "page", value = "페이지 번호", required = true, example = "1")
    @Min(1)
    int page;

    @ApiParam(name = "pageSize", value = "페이지 크기", required = true, example = "10")
    @Min(1)
    int pageSize;
}
