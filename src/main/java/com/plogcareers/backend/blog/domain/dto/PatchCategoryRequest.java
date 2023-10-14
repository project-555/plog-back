package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchCategoryRequest {
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

