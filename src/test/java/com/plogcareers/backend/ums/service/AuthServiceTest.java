package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.ums.domain.dto.ExitUserRequest;
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

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("updateUserProfile - 유저가 없는 경우 테스트")
    void testUpdateUserProfile_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(-1L).build()));
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
        Assertions.assertThrows(NotProperAuthorityException.class, () -> authService.updateUserProfile(-1L, UpdateUserProfileRequest.builder().userID(1L).build()));
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
        authService.updateUserProfile(1L, UpdateUserProfileRequest.builder().userID(1L).build());
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
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(-1L).build()));
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
        Assertions.assertThrows(NotProperAuthorityException.class, () -> authService.updateUserPassword(-1L, UpdateUserPasswordRequest.builder().userID(1L).build()));
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
        Assertions.assertThrows(IncorrectPasswordException.class, () -> authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("2").build()));
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
        authService.updateUserPassword(1L, UpdateUserPasswordRequest.builder().userID(1L).password("1").build());
        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("exitUser - 유저가 없는 경우 테스트")
    void testExitUser_1(){
        // given
        when(
                userRepository.findById(-1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.exitUser(1L, ExitUserRequest.builder().userID(-1L).build()));
    }

    @Test
    @DisplayName("exitUser - 로그인한 유저와 요청 유저가 다른 경우 테스트")
    void testExitUser_2(){
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
    void testExitUser_3(){
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
}
