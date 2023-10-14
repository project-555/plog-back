package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TagDTO {
    @ApiModelProperty(value = "태그 ID")
    private Long tagID;

    @ApiModelProperty(value = "태그 이름")
    private String tagName;
}
