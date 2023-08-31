package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {
    @ApiModelProperty(hidden = true)
    private Long blogID;
    @ApiModelProperty(hidden = true)
    private Long categoryID;
    @ApiModelProperty(hidden = true)
    private Long loginedUserID;

    @ApiModelProperty(value = "수정할 카테고리 이름")
    private String categoryName;

    @ApiModelProperty(value = "수정할 카테고리 설명")
    private String categoryDesc;
}

