package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {
    @NotNull
    @ApiParam(value = "카테고리 이름")
    private Long id;
    @NotNull
    @ApiParam(value = "카테고리 이름")
    private String categoryName;
    @ApiParam(value = "카테고리 설명")
    private String categoryDesc;

    public Category toCategoryEntity(Category category, Blog blog) {
        category.setCategoryName(this.categoryName);
        category.setCategoryDesc(this.categoryDesc);
        category.setBlog(blog);
        return category;
    }
}
