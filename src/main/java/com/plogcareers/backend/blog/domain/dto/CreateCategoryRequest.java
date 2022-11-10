package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotNull
    @ApiParam(value = "카테고리 이름")
    private String categoryName;
    @ApiParam(value = "카테고리 설명")
    private String categoryDesc;
    @NotNull
    @ApiParam(value = "정렬 순서")
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
