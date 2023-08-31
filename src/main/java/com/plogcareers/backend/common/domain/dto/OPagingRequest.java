package com.plogcareers.backend.common.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OPagingRequest {
    @ApiModelProperty(name = "page", value = "페이지 번호", required = true, example = "1")
    @Positive
    int page;

    @ApiModelProperty(name = "pageSize", value = "페이지 크기", required = true, example = "10")
    @Positive
    int pageSize;
}
