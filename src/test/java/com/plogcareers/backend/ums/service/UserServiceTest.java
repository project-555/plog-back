package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.GetUserResponse;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BlogRepository blogRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("getUser - 유저가 없는 경우 테스트")
    void testGetUser_1() {
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
    @DisplayName("getUser - 정상동작")
    void testGetUser_2() {
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
                        .id(1L)
                        .blogName("blogName")
                        .shortIntro("shortIntro")
                        .introHTML("introHTML")
                        .introMd("introMd")
                        .build())
        );
        // when
        GetUserResponse got = userService.getUser(1L);

        GetUserResponse want = GetUserResponse.builder()
                .email("email")
                .nickname("nickname")
                .profileImageURL("profileImageUrl")
                .blogID(1L)
                .blogName("blogName")
                .shortIntro("shortIntro")
                .introHTML("introHTML")
                .introMd("introMd")
                .build();
        // then
        Assertions.assertEquals(got, want);
    }

}
