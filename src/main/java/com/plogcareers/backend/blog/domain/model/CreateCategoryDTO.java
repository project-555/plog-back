package com.plogcareers.backend.blog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCategoryDTO {
    private String categoryName;
    private String categoryDesc;
}

