package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoryByBlogIdOrderByCategoryName(Long blogID);

    Boolean existsByBlogAndCategoryName(Blog blog, String categoryName);

    void deleteCategoryById(Long categoryId);
}
