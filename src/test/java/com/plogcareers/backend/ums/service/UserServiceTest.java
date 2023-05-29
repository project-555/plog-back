package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.GetUserInfoResponse;
import com.plogcareers.backend.ums.domain.dto.UpdateUserPasswordRequest;
import com.plogcareers.backend.ums.domain.dto.UpdateUserProfileRequest;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.IncorrectPasswordException;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
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
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BlogRepository blogRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("updateUserProfile - 유저가 없는 경우 테스트")
    void testUpdateUserProfile_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("updateUserProfile - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testUpdateUserProfile_2(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> userService.updateUserProfile(-1L, UpdateUserProfileRequest.builder().userID(1L).build()));
    }

    @Test
    @DisplayName("updateUserProfile - 정상동작")
    void testUpdateUserProfile_3(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when
        userService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(1L).build());
        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateUserPassword - 유저가 없는 경우 테스트")
    void testUpdateUserPassword_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("updateUserPassword - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testUpdateUserPassword_2(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> userService.updateUserPassword(-1L, UpdateUserPasswordRequest.builder().userID(1L).build()));
    }

    @Test
    @DisplayName("updateUserPassword - 현재 패스워드가 다른 경우 테스트")
    void testUpdateUserPassword_3(){
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
        Assertions.assertThrows(IncorrectPasswordException.class, () -> userService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("2").build()));
    }

    @Test
    @DisplayName("updateUserPassword - 정상동작")
    void testUpdateUserPassword_4(){
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
        userService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("1").build());
        // then
        verify(userRepository, times(1)).save(any());
    }


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
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(1L,-1L));
    }

    @Test
    @DisplayName("getUserInfo - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testGetUserInfo_2(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> userService.getUserInfo(2L, 1L));
    }

    @Test
    @DisplayName("getUserInfo - 정상동작")
    void testGetUserInfo_3(){
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
        GetUserInfoResponse got = userService.getUserInfo(1L, 1L);

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

    @Test
    @DisplayName("deleteUser - 유저가 없는 경우 테스트")
    void testDeleteUser_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(-1L, 1L));
    }

    @Test
    @DisplayName("deleteUser - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testDeleteUser_2(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> userService.deleteUser(1L, 2L));
    }

    @Test
    @DisplayName("deleteUser - 정상동작")
    void testDeleteUser_3(){
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );
        // when
        userService.deleteUser(1L, 1L);
        // then
        verify(userRepository, times(1)).delete(any());
    }
}
