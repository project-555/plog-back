package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoryByBlogIdOrderBySort(Long blogId);
    Boolean existsByBlogAndCategoryName(Blog blog, String categoryName);
}
