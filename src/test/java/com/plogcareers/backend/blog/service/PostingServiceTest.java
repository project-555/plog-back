package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.domain.model.PostingDTO;
import com.plogcareers.backend.blog.domain.model.PostingStarDTO;
import com.plogcareers.backend.blog.domain.model.PostingStarUserDTO;
import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.postgres.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostingServiceTest {
    @Mock
    BlogRepository blogRepository;
    @Mock
    PostingRepository postingRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    PostingTagRepository postingTagRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PostingStarRepository postingStarRepository;

    @Mock
    VPostingRepositorySupport postingRepositorySupport;

    @Mock
    CommentRepositorySupport commentRepositorySupport;

    @InjectMocks
    PostingService postingService;


    @Test
    @DisplayName("createPosting - 블로그가 없는 경우 테스트")
    void testCreatePosting_1() {
        // given
        when(
                blogRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.createPosting(-1L, 1L, new CreatePostingRequest()));
    }

    @Test
    @DisplayName("createPosting - 블로그 주인이 아닌 경우 테스트")
    void testCreatePosting_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(Optional.of(Blog.builder().id(1L).user(User.builder().id(2L).build()).build()));
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> postingService.createPosting(1L, 1L, new CreatePostingRequest()));
    }

    @Test
    @DisplayName("createPosting - 생성하려는 포스팅의 카테고리가 존재하지 않는 경우 테스트")
    void testCreatePosting_3() {
        //given
        when(blogRepository.findById(1L))
                .thenReturn(
                        Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                );
        when(categoryRepository.existsById(-1L)).thenReturn(false);

        // when + then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> postingService.createPosting(1L, 1L, CreatePostingRequest.builder().categoryID(-1L).build())
        );
    }

    @Test
    @DisplayName("createPosting - 정상 동작(태그 없음)")
    void testCreatePosting_4() {
        //given
        when(blogRepository.findById(1L))
                .thenReturn(
                        Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                );
        when(categoryRepository.existsById(1L))
                .thenReturn(true);
        when(postingRepository.save(any())).thenReturn(Posting.builder().id(1L).categoryID(1L).userID(1L).build());

        // when
        CreatePostingResponse got = postingService.createPosting(1L, 1L, CreatePostingRequest.builder().categoryID(1L).build());

        // then
        verify(tagRepository, never()).findByIdIn(any());
        verify(postingTagRepository, never()).saveAll(any());
        Assertions.assertEquals(CreatePostingResponse.builder().postingID(1L).build(), got);
    }

    @Test
    @DisplayName("createPosting - 정상 동작(태그 있음)")
    void testCreatePosting_5() {
        //given
        when(blogRepository.findById(1L))
                .thenReturn(
                        Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                );
        when(categoryRepository.existsById(1L))
                .thenReturn(true);
        when(tagRepository.findByIdIn(List.of(1L, 2L, 3L))).thenReturn(
                List.of(
                        Tag.builder().id(1L).blogID(1L).build(),
                        Tag.builder().id(2L).blogID(1L).build(),
                        Tag.builder().id(3L).blogID(1L).build()
                )
        );

        when(postingRepository.save(any())).thenReturn(Posting.builder().blogID(1L).id(1L).categoryID(1L).userID(1L).build());
        when(
                postingTagRepository.saveAll(
                        eq(
                                List.of(
                                        PostingTag.builder()
                                                .tag(Tag.builder().id(1L).blogID(1L).build())
                                                .posting(Posting.builder().blogID(1L).id(1L).categoryID(1L).userID(1L).build())
                                                .build(),
                                        PostingTag.builder()
                                                .tag(Tag.builder().id(2L).blogID(1L).build())
                                                .posting(Posting.builder().blogID(1L).id(1L).categoryID(1L).userID(1L).build())
                                                .build(),
                                        PostingTag.builder()
                                                .tag(Tag.builder().id(3L).blogID(1L).build())
                                                .posting(Posting.builder().blogID(1L).id(1L).categoryID(1L).userID(1L).build())
                                                .build()
                                )
                        )
                )
        ).thenReturn(null);
        // when
        CreatePostingResponse got = postingService.createPosting(1L, 1L, CreatePostingRequest.builder().tagIDs(List.of(1L, 2L, 3L)).categoryID(1L).build());

        // then
        verify(tagRepository, times(1)).findByIdIn(List.of(1L, 2L, 3L));
        verify(postingTagRepository, times(1)).saveAll(any());
        Assertions.assertEquals(CreatePostingResponse.builder().postingID(1L).build(), got);
    }

    @Test
    @DisplayName("getPosting - 블로그가 없는 경우 테스트")
    void getPosting_1() {
        // given
        when(blogRepository.findById(-1L)).thenReturn(Optional.empty());

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.getPosting(-1L, 1L, 1L));
    }

    @Test
    @DisplayName("getPosting - 조회할 포스팅이 없는 경우 테스트")
    void getPosting_2() {
        // given
        when(blogRepository.findById(1L)).thenReturn(Optional.of(
                Blog.builder().id(1L).build()
        ));
        when(postingRepository.findById(-1L)).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.getPosting(1L, -1L, 1L));
    }

    @Test
    @DisplayName("getPosting - 블로그 하위에 해당 포스팅이 속하지 않는 경우 테스트")
    void getPosting_3() {
        // given
        when(blogRepository.findById(1L)).thenReturn(Optional.of(
                Blog.builder().id(1L).build()
        ));
        when(postingRepository.findById(1L)).thenReturn(Optional.of(
                Posting.builder().id(1L).blogID(2L).build()
        ));
        // when + then
        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.getPosting(1L, 1L, 1L));
    }

    @Test
    @DisplayName("getPosting - 블로그 주인이 아니면서 공개 상태가 아닌 글을 조회 요청할 경우 테스트")
    void getPosting_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(postingRepository.findById(1L)).thenReturn(Optional.of(
                Posting.builder().id(1L).blogID(1L).stateID(State.PRIVATE).build()
        ));
        // when + then
        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.getPosting(1L, 1L, 2L));
    }

    @Test
    @DisplayName("getPosting - 정상동작 시")
    void getPosting_5() {
        // given
        when(blogRepository.findById(1L)).thenReturn(Optional.of(
                Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
        ));
        when(postingRepository.findById(1L)).thenReturn(Optional.of(
                Posting.builder()
                        .id(1L)
                        .blogID(1L)
                        .userID(1L)
                        .stateID(State.PUBLIC)
                        .hitCnt(1L)
                        .build()
        ));
        // when
        GetPostingResponse got = postingService.getPosting(1L, 1L, 1L);

        // then
        Assertions.assertEquals(got, GetPostingResponse.builder().id(1L).stateID(State.PUBLIC).hitCnt(2L).build());
    }

    @Test
    @DisplayName("ListPostings - 블로그가 없는 경우")
    void listPostings() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.listPostings(1L, 1L, ListPostingsRequest.builder().pageSize(5L).build()));
    }

    @Test
    @DisplayName("ListPostings - 정상 동작 시 (블로그 주인의 경우)")
    void listPostings_2() {
        ListPostingsRequest request = ListPostingsRequest.builder()
                .categoryID(1L)
                .search("test_search")
                .tagIDs(List.of(1L, 2L, 3L))
                .lastCursorID(3L)
                .pageSize(5L).build();
        List<PostingTag> testTags = List.of(
                PostingTag.builder().id(1L).build(),
                PostingTag.builder().id(2L).build(),
                PostingTag.builder().id(3L).build()
        );

        List<VPosting> testPostings = List.of(
                VPosting.builder().id(1L).build(),
                VPosting.builder().id(2L).build(),
                VPosting.builder().id(3L).build()
        );

        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );

        when(
                postingTagRepository.findByTag_IdIn(List.of(1L, 2L, 3L))
        ).thenReturn(
                testTags
        );

        when(
                postingRepositorySupport.listPostingsByOwner(1L, "test_search", 1L, testTags, 3L, 5L)
        ).thenReturn(
                testPostings
        );

        when(
                postingTagRepository.findByPostingIdIn(List.of(1L, 2L, 3L))
        ).thenReturn(
                List.of(
                        PostingTag.builder()
                                .posting(Posting.builder().id(1L).build())
                                .tag(Tag.builder().id(1L).tagName("HELLO").build())
                                .build()
                )
        );

        ListPostingsResponse got = postingService.listPostings(1L, 1L, request);


        ListPostingsResponse expected = ListPostingsResponse.builder()
                .postings(List.of(
                        PostingDTO.builder().id(1L).postingTags(List.of(PostingTagDTO.builder().tagID(1L).tagName("HELLO").build())).build(),
                        PostingDTO.builder().id(2L).postingTags(List.of()).build(),
                        PostingDTO.builder().id(3L).postingTags(List.of()).build()
                ))
                .build();

        Assertions.assertEquals(got, expected);
    }

    @Test
    @DisplayName("ListPostings - 정상 동작 시 (블로그 주인이 아닌 경우)")
    void listPostings_3() {
        ListPostingsRequest request = ListPostingsRequest.builder()
                .categoryID(1L)
                .search("test_search")
                .tagIDs(List.of(1L, 2L, 3L))
                .lastCursorID(3L)
                .pageSize(5L).build();

        List<PostingTag> testTags = List.of(
                PostingTag.builder().id(1L).build(),
                PostingTag.builder().id(2L).build(),
                PostingTag.builder().id(3L).build()
        );

        List<VPosting> testPostings = List.of(
                VPosting.builder().id(1L).build(),
                VPosting.builder().id(2L).build(),
                VPosting.builder().id(3L).build()
        );

        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(2L).build()).build()
                )
        );

        when(
                postingTagRepository.findByTag_IdIn(List.of(1L, 2L, 3L))
        ).thenReturn(
                testTags
        );

        when(
                postingRepositorySupport.listPostingsByUserAndGuest(1L, "test_search", 1L, testTags, 3L, 5L)
        ).thenReturn(
                testPostings
        );

        when(
                postingTagRepository.findByPostingIdIn(List.of(1L, 2L, 3L))
        ).thenReturn(
                List.of(
                        PostingTag.builder()
                                .posting(Posting.builder().id(1L).build())
                                .tag(Tag.builder().id(1L).tagName("HELLO").build())
                                .build()
                )
        );


        ListPostingsResponse got = postingService.listPostings(1L, 3L, request);

        ListPostingsResponse want = ListPostingsResponse.builder()
                .postings(List.of(
                        PostingDTO.builder().id(1L).postingTags(List.of(PostingTagDTO.builder().tagID(1L).tagName("HELLO").build())).build(),
                        PostingDTO.builder().id(2L).postingTags(List.of()).build(),
                        PostingDTO.builder().id(3L).postingTags(List.of()).build()
                ))
                .build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("updatePosting - 블로그가 없는 경우")
    void countPosting_1() {
        // given
        when(blogRepository.existsById(1L)).thenReturn(false);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.countPostings(CountPostingsRequest.builder().blogID(1L).build()));
    }

    @Test
    @DisplayName("정상 동작 시")
    void countPostings_2() {
        // given
        when(blogRepository.existsById(1L)).thenReturn(true);
        when(postingRepositorySupport.countPostings(1L, "test_search", List.of(1L, 2L, 3L))).thenReturn(1L);

        // when
        CountPostingsResponse got = postingService.countPostings(CountPostingsRequest.builder()
                .blogID(1L)
                .categoryIDs(List.of(1L, 2L, 3L))
                .search("test_search")
                .build());

        // then
        Assertions.assertEquals(CountPostingsResponse.builder().count(1L).build(), got);
    }


    @Test
    @DisplayName("ListPostingTags - 블로그가 없는 경우")
    void listPostingTags_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.listPostingTags(1L, 1L));

    }

    @Test
    @DisplayName("ListPostingTags - 포스팅이 없는 경우")
    void listPostingTags_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.listPostingTags(1L, 1L));
    }

    @Test
    @DisplayName("ListPostingTags - 블로그와 포스팅이 매치되지 않는 경우")
    void listPostingTags_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.listPostingTags(1L, 1L));
    }

    @Test
    @DisplayName("ListPostingTags - 정상 동작 시 (블로그 주인이 아닌 경우)")
    void listPostingTags_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );

        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );

        when(
                postingTagRepository.findByPostingId(1L)
        ).thenReturn(
                List.of(
                        PostingTag.builder().posting(Posting.builder().id(1L).build()).tag(Tag.builder().id(1L).tagName("HELLO").build()).build(),
                        PostingTag.builder().posting(Posting.builder().id(1L).build()).tag(Tag.builder().id(2L).tagName("PLOG").build()).build()
                )
        );


        ListPostingTagResponse got = postingService.listPostingTags(1L, 1L);

        ListPostingTagResponse want = ListPostingTagResponse.builder().postingTags(
                List.of(
                        PostingTagDTO.builder().tagID(1L).tagName("HELLO").build(),
                        PostingTagDTO.builder().tagID(2L).tagName("PLOG").build()
                )
        ).build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("deletePosting - 블로그가 없는 경우")
    void deletePosing_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.deletePosting(1L, 1L, 1L));
    }


    @Test
    @DisplayName("deletePosting - 로그인한 유저가 블로그 주인이 아닌 경우")
    void deletePosing_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(2L).build()).build()
                )
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> postingService.deletePosting(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePosting - 포스팅이 없는 경우")
    void deletePosing_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        // when + then
        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.deletePosting(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePosting - 블로그와 포스팅이 매치되지 않는 경우")
    void deletePosing_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        // when + then
        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.deletePosting(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePosting - 정상 동작 시")
    void deletePosing_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        // when
        postingService.deletePosting(1L, 1L, 1L);

        // then
        verify(postingRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("createComment - 블로그가 없는 경우")
    void createComment_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );
        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("createComment - 포스팅이 없는 경우")
    void createComment_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );
        // when + then
        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("createComment - 블로그와 포스팅이 매치되지 않는 경우")
    void createComment_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("createComment - 대댓글을 만드려고 하는데 대댓글의 부모가 존재하지 않는 경우")
    void createComment_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(ParentCommentNotFoundException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().parentCommentID(1L).build()));
    }

    @Test
    @DisplayName("createComment - 대대댓글을 만드려는 경우(허용하지 않는 기능)")
    void createComment_5() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(2L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().parentCommentID(1L).build()
                )
        );

        Assertions.assertThrows(InvalidParentExistException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().parentCommentID(2L).build()));
    }

    @Test
    @DisplayName("createComment - 댓글을 만드려는 유저가 존재하지 않는 경우")
    void createComment_6() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().id(1L).build()
                )
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(UserNotFoundException.class, () -> postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().parentCommentID(1L).build()));
    }

    @Test
    @DisplayName("createComment - 정상 동작 시")
    void createComment_7() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        User.builder().id(1L).build()
                )
        );

        postingService.createComment(1L, 1L, 1L, CreateCommentRequest.builder().build());

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateComment - 블로그가 없는 경우")
    void updateComment_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 포스팅이 없는 경우")
    void updateComment_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 블로그와 포스팅이 매치되지 않는 경우")
    void updateComment_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 댓글이 없는 경우")
    void updateComment_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(CommentNotFoundException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 댓글 주인이 아닌 경우")
    void updateComment_5() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().id(1L).user(User.builder().id(2L).build()).build()
                )
        );

        Assertions.assertThrows(NotProperAuthorityException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 포스팅과 댓글이 매치되지 않는 경우")
    void updateComment_6() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().postingID(2L).user(User.builder().id(1L).build()).build()
                )
        );

        Assertions.assertThrows(PostingCommentUnmatchedException.class, () -> postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build()));
    }

    @Test
    @DisplayName("updateComment - 정상 동작 시")
    void updateComment_7() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().postingID(1L).user(User.builder().id(1L).build()).build()
                )
        );

        postingService.updateComment(1L, 1L, 1L, 1L, UpdateCommentRequest.builder().build());

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("deleteComment - 블로그가 없는 경우")
    void deleteComment_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.deleteComment(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteComment - 포스팅이 없는 경우")
    void deleteComment_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.deleteComment(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteComment - 블로그와 포스팅이 매치되지 않는 경우")
    void deleteComment_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.deleteComment(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteComment - 댓글이 없는 경우")
    void deleteComment_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(CommentNotFoundException.class, () -> postingService.deleteComment(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteComment - 포스팅과 댓글이 매치되지 않는 경우")
    void deleteComment_5() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().postingID(2L).build()
                )
        );

        Assertions.assertThrows(PostingCommentUnmatchedException.class, () -> postingService.deleteComment(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteComment - 댓글 주인이 아닌 경우")
    void deleteComment_6() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).userID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().postingID(1L).user(User.builder().id(1L).build()).build()
                )
        );

        Assertions.assertThrows(NotProperAuthorityException.class, () -> postingService.deleteComment(1L, 1L, 1L, 2L));
    }

    @Test
    @DisplayName("deleteComment - 정상 동작 시")
    void deleteComment_7() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).userID(1L).build()
                )
        );
        when(
                commentRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Comment.builder().id(1L).postingID(1L).user(User.builder().id(1L).build()).build()
                )
        );

        postingService.deleteComment(1L, 1L, 1L, 1L);

        verify(commentRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("listComments - 블로그가 없는 경우")
    void listComments_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.listComments(1L, 1L, 1L));
    }

    @Test
    @DisplayName("listComments - 포스팅이 없는 경우")
    void listComments_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.listComments(1L, 1L, 1L));
    }

    @Test
    @DisplayName("listComments - 블로그와 포스팅이 매치되지 않는 경우")
    void listComments_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.listComments(1L, 1L, 1L));
    }

    @Test
    @DisplayName("listComments - 정상 동작 시")
    void listComments_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).nickname("test").build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).userID(1L).build()
                )
        );
        when(
                commentRepositorySupport.ListComments(1L)
        ).thenReturn(
                List.of(
                        Comment.builder().id(1L).postingID(1L).user(User.builder().id(1L).nickname("test").build()).isSecret(false).build(),
                        Comment.builder().id(2L).postingID(1L).user(User.builder().id(1L).nickname("test").build()).isSecret(false).build()
                )
        );

        ListCommentsResponse got = postingService.listComments(1L, 1L, 1L);
        ListCommentsResponse expected = ListCommentsResponse.builder()
                .comments(List.of(
                        Comment.builder().id(1L).postingID(1L).user(User.builder().id(1L).nickname("test").build()).isSecret(false).build().toCommentDTO(true, 1L),
                        Comment.builder().id(2L).postingID(1L).user(User.builder().id(1L).nickname("test").build()).isSecret(false).build().toCommentDTO(true, 1L)
                )).build();

        Assertions.assertEquals(got, expected);
    }

    @Test
    @DisplayName("listPostingStars - 블로그가 없는 경우")
    void listPostingStars_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.listPostingStars(1L, 1L));
    }

    @Test
    @DisplayName("listPostingStars - 포스팅이 없는 경우")
    void listPostingStars_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );
        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.listPostingStars(1L, 1L));
    }

    @Test
    @DisplayName("listPostingStars - 블로그와 포스팅이 매치되지 않는 경우")
    void listPostingStars_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.listPostingStars(1L, 1L));
    }

    @Test
    @DisplayName("listPostingStars - 정상 동작 시")
    void listPostingStars_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                postingStarRepository.findByPostingID(1L)
        ).thenReturn(
                List.of(
                        PostingStar.builder().id(1L).postingID(1L).user(User.builder().id(1L).build()).build(),
                        PostingStar.builder().id(2L).postingID(1L).user(User.builder().id(2L).build()).build()
                )
        );

        ListPostingStarsResponse got = postingService.listPostingStars(1L, 1L);
        ListPostingStarsResponse expected = ListPostingStarsResponse.builder()
                .postingStars(List.of(
                        PostingStarDTO.builder().id(1L).postingID(1L).user(PostingStarUserDTO.builder().id(1L).build()).build(),
                        PostingStarDTO.builder().id(2L).postingID(1L).user(PostingStarUserDTO.builder().id(2L).build()).build()
                ))
                .build();

        Assertions.assertEquals(got, expected);
    }

    @Test
    @DisplayName("createPostingStar - 블로그가 없는 경우")
    void createPostingStar_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );
        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.createPostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("createPostingStar - 포스팅이 없는 경우")
    void createPostingStar_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.createPostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("createPostingStar - 유저가 없는 경우")
    void createPostingStar_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(UserNotFoundException.class, () -> postingService.createPostingStar(1L, 1L, -1L));
    }

    @Test
    @DisplayName("createPostingStar - 블로그와 포스팅이 매치되지 않는 경우")
    void createPostingStar_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        User.builder().id(1L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.createPostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("createPostingStar - 정상 동작 시")
    void createPostingStar_5() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        User.builder().id(1L).build()
                )
        );

        postingService.createPostingStar(1L, 1L, 1L);

        verify(postingStarRepository, times(1)).save(PostingStar.builder().user(User.builder().id(1L).build()).postingID(1L).build());
    }

    @Test
    @DisplayName("deletePostingStar - 블로그가 없는 경우")
    void deletePostingStar_1() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(BlogNotFoundException.class, () -> postingService.deletePostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePostingStar - 포스팅이 없는 경우")
    void deletePostingStar_2() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingNotFoundException.class, () -> postingService.deletePostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePostingStar - 블로그와 포스팅이 매치되지 않는 경우")
    void deletePostingStar_3() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(2L).build()
                )
        );

        Assertions.assertThrows(BlogPostingUnmatchedException.class, () -> postingService.deletePostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePostingStar - 유저가 포스팅에 스타를 하지 않았던 경우")
    void deletePostingStar_4() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                postingStarRepository.findFirstByPostingIDAndUserId(1L, 1L)
        ).thenReturn(
                Optional.empty()
        );

        Assertions.assertThrows(PostingStarNotFoundException.class, () -> postingService.deletePostingStar(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deletePostingStar - 정상 동작 시")
    void deletePostingStar_5() {
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                postingRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Posting.builder().id(1L).blogID(1L).build()
                )
        );
        when(
                postingStarRepository.findFirstByPostingIDAndUserId(1L, 1L)
        ).thenReturn(
                Optional.of(
                        PostingStar.builder().id(1L).build()
                )
        );

        postingService.deletePostingStar(1L, 1L, 1L);

        verify(postingStarRepository, times(1)).delete(PostingStar.builder().id(1L).build());
    }
}