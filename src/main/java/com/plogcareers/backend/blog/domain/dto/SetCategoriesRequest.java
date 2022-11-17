package com.plogcareers.backend.blog.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plogcareers.backend.blog.domain.model.CreateCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetCategoriesRequest {
    @JsonProperty("categories")
    private List<CreateCategoryDTO> categories;
}
