package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateCategoryRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.CategoryDuplicatedException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    public void createCategory(@NotNull CreateCategoryRequest createCategoryRequest) throws CategoryDuplicatedException {
        Blog blog = blogRepository.findById(createCategoryRequest.getBlogId()).orElseThrow(BlogNotFoundException::new);
        if (categoryRepository.existsByBlogAndCategoryName(blog, createCategoryRequest.getCategoryName())) {
            throw new CategoryDuplicatedException();
        }
        categoryRepository.save(createCategoryRequest.toEntity(blog));
    }
}
