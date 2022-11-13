package com.plogcareers.backend.blog.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateCategoriesRequest {
    @JsonProperty("categories")
    private List<CategoryUpdateDTO> categoriesUpdateDTO;
}
