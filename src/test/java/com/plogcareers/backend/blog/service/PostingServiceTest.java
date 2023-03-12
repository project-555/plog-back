package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreatePostingRequest;
import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
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
        Assertions.assertThrows(BlogNotFoundException.class, () -> {
            postingService.createPosting(-1L, 1L, new CreatePostingRequest());
        });
    }

    @Test
    @DisplayName("createPosting - 블로그 주인이 아닌 경우 테스트")
    void testCreatePosting_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(Optional.of(Blog.builder().id(1L).user(User.builder().id(2L).build()).build()));
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> {
            postingService.createPosting(1L, 1L, new CreatePostingRequest());
        });
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
                () -> {
                    postingService.createPosting(
                            1L, 1L,
                            CreatePostingRequest.builder().categoryID(-1L).build()
                    );
                }
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
        Long got = postingService.createPosting(1L, 1L, CreatePostingRequest.builder().categoryID(1L).build());

        // then
        verify(tagRepository, never()).findByIdIn(any());
        verify(postingTagRepository, never()).saveAll(any());
        Assertions.assertEquals(1L, got);
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
        Long got = postingService.createPosting(1L, 1L, CreatePostingRequest.builder().tagIDs(List.of(1L, 2L, 3L)).categoryID(1L).build());

        // then
        verify(tagRepository, times(1)).findByIdIn(List.of(1L, 2L, 3L));
        verify(postingTagRepository, times(1)).saveAll(any());
        Assertions.assertEquals(1L, got);
    }

    @Test
    @DisplayName("getPosting - 블로그가 없는 경우 테스트")
    void getPosting_1() {
        // given
        when(blogRepository.findById(-1L)).thenReturn(Optional.empty());

        // when + then
        Assertions.assertThrows(
                BlogNotFoundException.class,
                () -> {
                    postingService.getPosting(-1L, 1L, 1L);
                }
        );
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
        Assertions.assertThrows(
                PostingNotFoundException.class,
                () -> {
                    postingService.getPosting(1L, -1L, 1L);
                }
        );
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
        Assertions.assertThrows(
                BlogPostingUnmatchedException.class,
                () -> {
                    postingService.getPosting(1L, 1L, 1L);
                }
        );
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
        Assertions.assertThrows(PostingNotFoundException.class,
                () -> {
                    postingService.getPosting(1L, 1L, 2L);
                }
        );
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
                        .build()
        ));
        // when
        GetPostingResponse got = postingService.getPosting(1L, 1L, 1L);

        // then
        Assertions.assertEquals(got, GetPostingResponse.builder().id(1L).stateID(State.PUBLIC).build());
    }
}