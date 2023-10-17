package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;
import java.util.List;


class CommentRepositorySupportTest extends BaseRepositorySupportTest {
    CommentRepositorySupport commentRepositorySupport;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostingRepository postingRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    StateRepository stateRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void init() {
        commentRepositorySupport = new CommentRepositorySupport(testEntityManager.getEntityManager());
    }

    @Test
    void listComments() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("test")
                .firstName("test")
                .lastName("test")
                .nickname("test")
                .joinDt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Blog blog = Blog.builder()
                .id(1L)
                .blogName("test")
                .user(user)
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();
        blogRepository.save(blog);

        Category category = Category.builder()
                .id(1L)
                .blog(blog)
                .categoryName("test")
                .categoryDesc("test")
                .build();

        categoryRepository.save(category);

        State state = State.builder()
                .id(1L)
                .stateName("test")
                .build();
        stateRepository.save(state);


        Posting posting = Posting.builder()
                .id(1L)
                .blogID(1L)
                .categoryID(1L)
                .userID(1L)
                .title("test")
                .mdContent("test")
                .htmlContent("test")
                .stateID(1L)
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();

        postingRepository.save(posting);

        List<Comment> want = List.of(
                Comment.builder()
                        .id(1L)
                        .user(user)
                        .postingID(1L)
                        .commentContent("test_parant_comment")
                        .parentCommentID(null)
                        .isSecret(false)
                        .createDt(LocalDateTime.now())
                        .updateDt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .id(2L)
                        .user(user)
                        .postingID(1L)
                        .commentContent("test_child_comment")
                        .parentCommentID(1L)
                        .isSecret(false)
                        .createDt(LocalDateTime.now())
                        .updateDt(LocalDateTime.now())
                        .build()
        );

        // given
        commentRepository.saveAll(want);

        // when
        List<Comment> got = commentRepositorySupport.ListComments(1L);

        // then
        Assertions.assertEquals(want, got);
    }
}