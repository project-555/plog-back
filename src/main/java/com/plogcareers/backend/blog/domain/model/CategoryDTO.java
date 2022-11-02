package com.plogcareers.backend.blog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private int sort;
}
