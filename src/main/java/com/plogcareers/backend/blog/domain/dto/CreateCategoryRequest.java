package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotNull
    private String categoryName;
    private String categoryDesc;
    @NotNull
    private int sort;
    @NotNull
    private Long blogId;

    public Category toEntity(Blog blog) {
        return Category.builder()
                .categoryName(categoryName)
                .categoryDesc(categoryDesc)
                .sort(sort)
                .blog(blog)
                .build();
    }
}
