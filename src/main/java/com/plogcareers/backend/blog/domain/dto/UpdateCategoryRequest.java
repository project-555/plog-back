package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.CategoryUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {
    private Long blogId;
    private List<CategoryUpdateDTO> categoryUpdateDTOList;
}
