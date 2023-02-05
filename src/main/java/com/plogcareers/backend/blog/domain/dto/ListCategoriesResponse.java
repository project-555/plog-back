package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoriesResponse {
    @ApiModelProperty(value = "카테고리 리스트")
    List<CategoryDTO> categories;
}
