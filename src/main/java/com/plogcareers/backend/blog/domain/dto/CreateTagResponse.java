package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTagResponse {
    @ApiModelProperty(value = "생성된 태그 ID")
    Long tagID;
    @ApiModelProperty(value = "생성된 태그 이름")
    String tagName;
}
