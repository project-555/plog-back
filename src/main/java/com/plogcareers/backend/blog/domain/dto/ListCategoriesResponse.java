package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ListCategoriesResponse {
    @ApiModelProperty(value = "카테고리 리스트")
    List<CategoryDTO> categories;
}
