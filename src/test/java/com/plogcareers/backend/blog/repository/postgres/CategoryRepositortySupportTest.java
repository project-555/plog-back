package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.config.DatabaseTestConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
@ContextConfiguration(classes = DatabaseTestConfig.class)
class CategoryRepositortySupportTest extends BaseRepositorySupportTest {

    @Autowired
    TestEntityManager testEntityManager;

    CategoryRepositortySupport categoryRepositortySupport;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void init() {
        categoryRepositortySupport = new CategoryRepositortySupport(testEntityManager.getEntityManager());
    }

    @Test
    @DisplayName("existsDuplicatedCategory - 카테고리 중복 있음")
    void existsDuplicatedCategory_1() {
        // given
        Blog blog = blogRepository.save(Blog.builder().id(1L).blogName("test").build());
        Category category = categoryRepository.save(Category.builder().id(1L).categoryName("test").categoryDesc("test").blog(blog).build());

        // when
        boolean result = categoryRepositortySupport.existsDuplicatedCategory(blog.getId(), 2L, category.getCategoryName());

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
        boolean result = categoryRepositortySupport.existsDuplicatedCategory(blog.getId(), category.getId(), category.getCategoryName());

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
        boolean result = categoryRepositortySupport.existsDuplicatedCategory(blog.getId(), 2L, "test2");

        // then
        Assertions.assertFalse(result);
    }
}