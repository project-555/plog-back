package com.plogcareers.backend.blog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryUpdateDTO {
    private String categoryName;
    private String categoryDesc;
    private int sort;
}
