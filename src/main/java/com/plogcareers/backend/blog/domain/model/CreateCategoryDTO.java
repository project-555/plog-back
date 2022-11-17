package com.plogcareers.backend.blog.domain.model;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCategoryDTO {
    private String categoryName;
    private String categoryDesc;
    private int sort;

    public Category toEntity(Blog blog) {
        return Category.builder()
                .categoryDesc(this.categoryDesc)
                .categoryName(this.categoryName)
                .sort(this.sort)
                .blog(blog)
                .build();
    }
}

