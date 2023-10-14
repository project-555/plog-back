package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.DBConnectionProvider;
import com.plogcareers.backend.blog.domain.entity.Tag;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.repository.postgres.*;
import com.plogcareers.backend.config.DatabaseTestConfig;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
@ContextConfiguration(classes = DatabaseTestConfig.class)
public class VPostingRepositorySupportTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.2"
    ).withInitScript("schema-postgres.sql");
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

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
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
