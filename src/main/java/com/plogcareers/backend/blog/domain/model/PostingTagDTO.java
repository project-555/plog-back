package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class PostingTagDTO {
    @ApiModelProperty(value = "포스팅에 달린 태그 ID")
    private Long tagID;

    @ApiModelProperty(value = "포스팅에 달린 태그 이름")
    private String tagName;
}
