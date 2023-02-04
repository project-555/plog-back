package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateSubscribeRequest;
import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;
import com.plogcareers.backend.blog.domain.dto.ListHomePostingsResponse;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Subscribe;
import com.plogcareers.backend.blog.domain.entity.VHotPosting;
import com.plogcareers.backend.blog.domain.model.HomePostingDTO;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.SubscribeRepository;
import com.plogcareers.backend.blog.repository.VHotPostingRepositorySupport;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HomeServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BlogRepository blogRepository;
    @Mock
    SubscribeRepository subscribeRepository;
    @Mock
    VHotPostingRepositorySupport vHotPostingRepositorySupport;

    @InjectMocks
    HomeService homeService;


    @Test
    @DisplayName("createSubscribe - 블로그가 없는 경우 테스트")
    void testCreateSubscribe_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> homeService.createSubscribe(1L, CreateSubscribeRequest.builder().blogId(1L).build()));
    }

    @Test
    @DisplayName("createSubscribe - 유저가 없는 경우 테스트")
    void testCreateSubscribe_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).build())
        );
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> homeService.createSubscribe(-1L, CreateSubscribeRequest.builder().blogId(1L).userId(-1L).build()));
    }

    @Test
    @DisplayName("createSubscribe - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testCreateSubscribe_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).build())
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> homeService.createSubscribe(-1L, CreateSubscribeRequest.builder().blogId(1L).userId(1L).build()));
    }

    @Test
    @DisplayName("createSubscribe - 본인을 구독하는 경우 테스트")
    void testCreateSubscribe_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(SelfSubscribeException.class, () -> homeService.createSubscribe(1L, CreateSubscribeRequest.builder().blogId(1L).userId(1L).build()));
    }

    @Test
    @DisplayName("createSubscribe - 중복으로 블로그를 구독하는 경우 테스트")
    void testCreateSubscribe_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(2L).build()).build())
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        when(
                subscribeRepository.existsByUserIdAndBlogId(1L, 1L)
        ).thenReturn(true);
        // when + then
        Assertions.assertThrows(SubscribeDuplicatedException.class, () -> homeService.createSubscribe(1L, CreateSubscribeRequest.builder().blogId(1L).userId(1L).build()));
    }

    @Test
    @DisplayName("createSubscribe - 정상동작")
    void testCreateSubscribe_6() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(2L).build()).build())
        );
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when
        homeService.createSubscribe(1L, CreateSubscribeRequest.builder().userId(1L).blogId(1L).build());

        // then
        verify(subscribeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("deleteSubscribe - 유저가 없는 경우 테스트")
    void testDeleteSubscribe_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).build())
        );
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> homeService.createSubscribe(-1L, CreateSubscribeRequest.builder().blogId(1L).userId(-1L).build()));
    }

    @Test
    @DisplayName("deleteSubscribe - 구독 정보가 없는 경우 테스트")
    void testDeleteSubscribe_2() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        when(
                subscribeRepository.findById(-1L)
        ).thenReturn(
                Optional.empty()
        );
        // when + then
        Assertions.assertThrows(SubscribeNotFoundException.class, () -> homeService.deleteSubscribe(-1L, 1L));
    }

    @Test
    @DisplayName("deleteSubscribe - 로그인한 유저와 블로그를 구독하고 있는 유저정보가 다른 경우 테스트")
    void testDeleteSubscribe_3() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        when(
                subscribeRepository.findById(1L)
        ).thenReturn(
                Optional.of(Subscribe.builder().id(1L).userId(2L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> homeService.deleteSubscribe(1L, 1L));
    }

    @Test
    @DisplayName("deleteSubscribe - 정상동작")
    void testDeleteSubscribe_4() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        when(
                subscribeRepository.findById(1L)
        ).thenReturn(
                Optional.of(Subscribe.builder().id(1L).userId(1L).build())
        );
        // when
        homeService.deleteSubscribe(1L, 1L);

        // then
        verify(subscribeRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("listFollowingPostings - 정상동작")
    void testListFollowingPostings_1() {
        // given
        when(
                subscribeRepository.findByUserId(1L)
        ).thenReturn(
                List.of(
                        Subscribe.builder().blogId(1L).build(),
                        Subscribe.builder().blogId(2L).build(),
                        Subscribe.builder().blogId(3L).build()
                )
        );
        when(
                vHotPostingRepositorySupport.listFollowingHomePostings(1L, List.of(1L, 2L, 3L), 10)
        ).thenReturn(
                List.of(
                        VHotPosting.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .title("title1")
                                .summary("summary1")
                                .thumbnailImageUrl("thumbnailImageUrl1")
                                .createDt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                        .build(),
                        VHotPosting.builder()
                                .id(2L)
                                .user(User.builder().id(2L).build())
                                .title("title2")
                                .summary("summary2")
                                .thumbnailImageUrl("thumbnailImageUrl2")
                                .createDt(LocalDateTime.of(2021, 1, 2, 0, 0, 0))
                        .build()
                )
        );
        // when
        ListHomePostingsResponse got = homeService.listFollowingPostings(1L, 1L, 10);

        ListHomePostingsResponse want = ListHomePostingsResponse.builder().homePostings(
                List.of(
                        HomePostingDTO.builder().postingID(1L)
                                .homePostingUser(HomePostingUserDTO.builder().userID(1L).build())
                                .title("title1")
                                .summary("summary1")
                                .thumbnailImageUrl("thumbnailImageUrl1")
                                .createDt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                                .build(),
                        HomePostingDTO.builder().postingID(2L)
                                .homePostingUser(HomePostingUserDTO.builder().userID(2L).build())
                                .title("title2")
                                .summary("summary2")
                                .thumbnailImageUrl("thumbnailImageUrl2")
                                .createDt(LocalDateTime.of(2021, 1, 2, 0, 0, 0))
                                .build()
                )
        ).build();
        // then
        Assertions.assertEquals(got, want);
    }
}
