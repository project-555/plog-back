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
import java.time.format.DateTimeFormatter;
import java.util.List;


public class VPostingRepositorySupportTest extends BaseRepositorySupportTest {

    @Autowired
    TestEntityManager testEntityManager;
    VPostingRepositorySupport vPostingRepositorySupport;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private PostingRepository postingRepository;
    @Autowired
    private PostingTagRepository postingTagRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void init() {
        vPostingRepositorySupport = new VPostingRepositorySupport(testEntityManager.getEntityManager());
    }

    @Test
    void VPostingRepositorySupportTest_1() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        String stringDateTime = localDateTime.format(formatter);
        LocalDateTime now = LocalDateTime.parse(stringDateTime, formatter);


        // given
        User user = userRepository.save(User.builder().id(1L).firstName("test").lastName("test").email("test@email.com").password("test").joinDt(now).build());
        Blog blog = blogRepository.save(Blog.builder().id(1L).blogName("test").user(user).build());
        Category category = categoryRepository.save(Category.builder().id(1L).categoryName("test").categoryDesc("test").blog(blog).build());
        Tag tag = tagRepository.save(Tag.builder().id(1L).tagName("test").blogID(blog.getId()).build());
        State state = stateRepository.save(State.builder().id(1L).stateName("test").build());
        Posting posting = postingRepository.save(Posting.builder().categoryID(category.getId()).title("test").htmlContent("test").mdContent("test").createDt(now).updateDt(now).isStarAllowed(true).isCommentAllowed(true).stateID(1L).blogID(blog.getId()).userID(user.getId()).build());
        PostingTag postingTag = postingTagRepository.save(PostingTag.builder().id(1L).tag(tag).posting(posting).build());

        // when
        VPosting want = VPosting.builder().id(1L).categoryID(1L).title("test").htmlContent("test").mdContent("test").createDt(now).updateDt(now).isStarAllowed(true).isCommentAllowed(true).stateID(1L).blogID(1L).hitCnt(0L).postingStarCount(0L).user(User.builder().id(1L).firstName("test").lastName("test").email("test@email.com").password("test").joinDt(now).build()).build();
        List<PostingTag> postingTags = postingTagRepository.findByPostingId(posting.getId());
        List<VPosting> got = vPostingRepositorySupport.listPostingsByUserAndGuest(blog.getId(), null, category.getId(), postingTags, null, 1L);

        // then
        Assertions.assertEquals(want, got.get(0));

    }
}
