package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotNull
    @ApiModelProperty(value = "카테고리 이름")
    private String categoryName;
    
    @ApiModelProperty(value = "카테고리 설명")
    private String categoryDesc;

    public Category toEntity(Blog blog) {
        return Category.builder()
                .categoryName(categoryName)
                .categoryDesc(categoryDesc)
                .blog(blog)
                .build();
    }
}
