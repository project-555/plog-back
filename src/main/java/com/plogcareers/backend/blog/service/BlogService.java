package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateCategoryRequest;
import com.plogcareers.backend.blog.domain.dto.UpdateCategoriesRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.model.CategoryUpdateDTO;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.CategoryDuplicatedException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    public void createCategory(@NotNull CreateCategoryRequest createCategoryRequest) throws BlogNotFoundException, CategoryDuplicatedException {
        Blog blog = blogRepository.findById(createCategoryRequest.getBlogId()).orElseThrow(BlogNotFoundException::new);
        if (categoryRepository.existsByBlogAndCategoryName(blog, createCategoryRequest.getCategoryName())) {
            throw new CategoryDuplicatedException();
        }
        categoryRepository.save(createCategoryRequest.toEntity(blog));
    }

    @Transactional
    public void setCategory(Long blogId, @NotNull UpdateCategoriesRequest request) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(BlogNotFoundException::new);
        categoryRepository.deleteCategoryByBlogId(blogId);
        for (CategoryUpdateDTO categoryUpdateDTO : request.getCategoriesUpdateDTO()) {
            categoryRepository.save(
                    Category.builder()
                            .categoryName(categoryUpdateDTO.getCategoryName())
                            .categoryDesc(categoryUpdateDTO.getCategoryDesc())
                            .sort(categoryUpdateDTO.getSort())
                            .blog(blog)
                            .build()
            );
        }
    }
}
