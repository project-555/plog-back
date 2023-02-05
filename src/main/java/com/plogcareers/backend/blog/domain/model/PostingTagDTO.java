package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostingTagDTO {
    @ApiModelProperty(value = "포스팅에 달린 태그 ID")
    private Long tagId;

    @ApiModelProperty(value = "포스팅에 달린 태그 이름")
    private String tagName;
}
