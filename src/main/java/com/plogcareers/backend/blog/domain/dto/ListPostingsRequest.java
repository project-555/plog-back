package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListPostingsRequest {
    @ApiParam(value = "검색어")
    String search;
    @ApiParam(value = "검색할 태그 ID 리스트")
    List<Long> tagIDs;
    @ApiParam(value = "검색할 카테고리 ID")
    Long categoryID;

    @ApiParam(value = "이전 포스팅 리스트의 마지막 포스팅 ID")
    Long lastCursorID;

    @NotNull
    @Range(min = 1, max = 20)
    @ApiParam(value = "페이지 당 포스팅 수")
    Long pageSize;
}
