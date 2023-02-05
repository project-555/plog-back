package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {
    @ApiModelProperty(value = "카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(value = "카테고리 이름")
    private String categoryName;

    @ApiModelProperty(value = "카테고리 설명")
    private String categoryDesc;
}
