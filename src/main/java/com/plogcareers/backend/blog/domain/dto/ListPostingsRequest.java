package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ListPostingsRequest {
    @ApiModelProperty(value = "검색어")
    String search;
    @ApiModelProperty(value = "검색할 태그 ID 리스트")
    List<Long> tagIDs;
    @ApiModelProperty(value = "검색할 카테고리 ID", example = "1")
    Long categoryID;

    @ApiModelProperty(value = "이전 포스팅 리스트의 마지막 포스팅 ID", example = "1")
    Long lastCursorID;

    @NotNull
    @Range(min = 1, max = 20)
    @ApiModelProperty(value = "페이지 당 포스팅 수", example = "10")
    Long pageSize;
}
