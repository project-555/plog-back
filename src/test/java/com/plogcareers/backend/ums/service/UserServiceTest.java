package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.GetUserInfoResponse;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BlogRepository blogRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("getUserInfo - 유저가 없는 경우 테스트")
    void testGetUserInfo_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(
                Optional.empty()
        );
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser(-1L));
    }

    @Test
    @DisplayName("getUserInfo - 정상동작")
    void testGetUserInfo_2(){
        User testUser = User.builder()
                .id(1L)
                .email("email")
                .nickname("nickname")
                .profileImageURL("profileImageUrl")
                .build();
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(testUser)
        );
        when(
                blogRepository.findByUser(testUser)
        ).thenReturn(
                List.of(Blog.builder()
                        .blogName("blogName")
                        .shortIntro("shortIntro")
                        .introHTML("introHtml")
                        .build())
        );
        // when
        GetUserInfoResponse got = userService.getUser(1L);

        GetUserInfoResponse want = GetUserInfoResponse.builder()
                                                    .email("email")
                                                    .nickname("nickname")
                                                    .profileImageUrl("profileImageUrl")
                                                    .blogName("blogName")
                                                    .shortIntro("shortIntro")
                                                    .introHtml("introHtml")
                                                    .build();
        // then
        Assertions.assertEquals(got, want);
    }

}
