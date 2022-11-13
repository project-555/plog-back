package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoryResponse {
    List<CategoryDTO> categories;
}
