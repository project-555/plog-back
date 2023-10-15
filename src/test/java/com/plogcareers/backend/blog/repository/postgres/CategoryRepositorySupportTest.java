package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

class CategoryRepositorySupportTest extends BaseRepositorySupportTest {


    @Autowired
    TestEntityManager testEntityManager;
    CategoryRepositorySupport categoryRepositorySupport;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void init() {
        categoryRepositorySupport = new CategoryRepositorySupport(testEntityManager.getEntityManager());
    }

    @Test
    @DisplayName("existsDuplicatedCategory - 빈 카테고리 명으로 요청")
    void existsDuplicatedCategory_0() {
        // given + when
        boolean result = categoryRepositorySupport.existsDuplicatedCategory(1L, 2L, "");

        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("existsDuplicatedCategory - 카테고리 중복 있음")
    void existsDuplicatedCategory_1() {
        // given
        Blog blog = blogRepository.save(Blog.builder().id(1L).blogName("test").build());
        Category category = categoryRepository.save(Category.builder().id(1L).categoryName("test").categoryDesc("test").blog(blog).build());

        // when
        boolean result = categoryRepositorySupport.existsDuplicatedCategory(blog.getId(), 2L, category.getCategoryName());

        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("existsDuplicatedCategory - 자기 자신 카테고리만 조회됨")
    void existsDuplicatedCategory_2() {
        // given
        Blog blog = blogRepository.save(Blog.builder().id(1L).blogName("test").build());
        Category category = categoryRepository.save(Category.builder().id(1L).categoryName("test").categoryDesc("test").blog(blog).build());

        // when
        boolean result = categoryRepositorySupport.existsDuplicatedCategory(blog.getId(), category.getId(), category.getCategoryName());

        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("existsDuplicatedCategory - 카테고리 중복 없음")
    void existsDuplicatedCategory_3() {
        // given
        Blog blog = blogRepository.save(Blog.builder().id(1L).blogName("test").build());
        Category category = categoryRepository.save(Category.builder().id(1L).categoryName("test").categoryDesc("test").blog(blog).build());

        // when
        boolean result = categoryRepositorySupport.existsDuplicatedCategory(blog.getId(), 2L, "test2");

        // then
        Assertions.assertFalse(result);
    }
}