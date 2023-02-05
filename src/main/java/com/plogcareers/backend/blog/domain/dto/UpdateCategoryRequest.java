package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "카테고리 ID")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "수정할 카테고리 이름")
    private String categoryName;
    
    @ApiModelProperty(value = "수정할 카테고리 설명")
    private String categoryDesc;

    public Category toCategoryEntity(Category category, Blog blog) {
        category.setCategoryName(this.categoryName);
        category.setCategoryDesc(this.categoryDesc);
        category.setBlog(blog);
        return category;
    }
}
