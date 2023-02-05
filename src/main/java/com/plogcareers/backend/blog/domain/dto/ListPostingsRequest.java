package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
