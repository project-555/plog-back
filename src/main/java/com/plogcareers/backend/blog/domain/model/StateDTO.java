package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StateDTO {
    @ApiModelProperty(value = "포스팅 상태 ID")
    public Long id;

    @ApiModelProperty(value = "포스팅 상태 이름")
    public String stateName;
}
