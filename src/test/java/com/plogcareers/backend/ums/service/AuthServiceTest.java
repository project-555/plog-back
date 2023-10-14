package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.dto.RefreshAccessTokenResponse;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.repository.postgres.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.ExitUserRequest;
import com.plogcareers.backend.ums.domain.dto.Token;
import com.plogcareers.backend.ums.domain.dto.UpdateUserPasswordRequest;
import com.plogcareers.backend.ums.domain.dto.UpdateUserProfileRequest;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.IncorrectPasswordException;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BlogRepository blogRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("updateUserProfile - 유저가 없는 경우 테스트")
    void testUpdateUserProfile_1() {
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("updateUserProfile - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testUpdateUserProfile_2() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> authService.updateUserProfile(-1L, UpdateUserProfileRequest.builder().userID(1L).build()));
    }

    @Test
    @DisplayName("updateUserProfile - 정상동작")
    void testUpdateUserProfile_3() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when
        authService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(1L).build());
        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateUserPassword - 유저가 없는 경우 테스트")
    void testUpdateUserPassword_1() {
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("updateUserPassword - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testUpdateUserPassword_2() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> authService.updateUserPassword(-1L, UpdateUserPasswordRequest.builder().userID(1L).build()));
    }

    @Test
    @DisplayName("updateUserPassword - 현재 패스워드가 다른 경우 테스트")
    void testUpdateUserPassword_3() {
        // given
        when(
                passwordEncoder.matches("2", "1")
        ).thenReturn(false);
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).password("1").build())
        );
        // when + then
        Assertions.assertThrows(IncorrectPasswordException.class, () -> authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("2").build()));
    }

    @Test
    @DisplayName("updateUserPassword - 정상동작")
    void testUpdateUserPassword_4() {
        // given
        when(
                passwordEncoder.matches("1", "1")
        ).thenReturn(true);
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).password("1").build())
        );
        // when
        authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("1").build());
        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("exitUser - 유저가 없는 경우 테스트")
    void testExitUser_1() {
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.exitUser(1L, ExitUserRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("exitUser - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testExitUser_2() {
        // given
        when(
                userRepository.findById(2L)
        ).thenReturn(
                Optional.of(User.builder().id(2L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> authService.exitUser(1L, ExitUserRequest.builder().userID(2L).build()));
    }

    @Test
    @DisplayName("exitUser - 정상동작")
    void testExitUser_3() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when
        authService.exitUser(1L, ExitUserRequest.builder().userID(1L).build());
        // then
        verify(userRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("refreshAccessToken - 없는 유저의 경우 에러")
    void testRefreshAccessToken_1() {
        when(
                userRepository.findById(100L)
        ).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> authService.refreshAccessToken(100L));
    }

    @Test
    @DisplayName("refreshAccessToken - 유저의 블로그가 없는 경우 에러")
    void testRefreshAccessToken_2() {
        User testUser = User.builder().id(100L).build();
        when(
                userRepository.findById(100L)
        ).thenReturn(Optional.of(testUser));
        when(
                blogRepository.findByUser(testUser)
        ).thenReturn(List.of());

        Assertions.assertThrows(BlogNotFoundException.class, () -> authService.refreshAccessToken(100L));
    }

    @Test
    @DisplayName("refreshAccessToken - 정상동작")
    void testRefreshAccessToken_3() {
        User testUser = User.builder().id(100L).build();
        Blog testBlog = Blog.builder().id(100L).user(testUser).build();
        when(
                userRepository.findById(100L)
        ).thenReturn(Optional.of(testUser));
        when(
                blogRepository.findByUser(testUser)
        ).thenReturn(List.of(testBlog));
        when(
                jwtTokenProvider.createToken(testUser.getUsername(), testUser, testBlog.getId())
        ).thenReturn("testToken");

        Assertions.assertEquals(
                RefreshAccessTokenResponse
                        .builder()
                        .token(new Token("testToken"))
                        .build(), authService.refreshAccessToken(100L));
    }
}
