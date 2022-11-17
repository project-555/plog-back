package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateCategoryRequest;
import com.plogcareers.backend.blog.domain.dto.ListCategoryResponse;
import com.plogcareers.backend.blog.domain.dto.SetCategoriesRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.CategoryDuplicatedException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    // 카테고리 가져오기
    public ListCategoryResponse listCategory(Long blogId) throws BlogNotFoundException {
        if (blogRepository.existsById(blogId)) {
            List<CategoryDTO> categoryList = categoryRepository.findCategoryByBlogIdOrderBySort(blogId)
                    .stream()
                    .map(Category::toCategoryDto)
                    .toList();
            return ListCategoryResponse.builder()
                    .categories(categoryList)
                    .build();
        }
        throw new BlogNotFoundException();
    }

    // 카테고리 다건 수정하기
    @Transactional
    public void setCategories(Long blogId, @NotNull SetCategoriesRequest request) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(BlogNotFoundException::new);
        categoryRepository.deleteCategoryByBlogId(blogId);
        List<Category> categories = request.getCategories().stream()
                        .map(createCategoryDTO -> createCategoryDTO.toEntity(blog))
                        .toList();
        categoryRepository.saveAll(categories);
        }
    }

