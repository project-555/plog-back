package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CountPostingsRequest {
    @ApiModelProperty(hidden = true)
    private Long blogID;
    private List<Long> categoryIDs;
    private String search;
}
