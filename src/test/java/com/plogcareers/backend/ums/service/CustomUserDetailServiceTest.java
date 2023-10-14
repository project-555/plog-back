package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {
    @InjectMocks
    CustomUserDetailService customUserDetailService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("loadUserByUsername - 유저가 없음")
    void loadUserByUsername() {
        // given
        when(
                userRepository.findByEmail("email")
        ).thenReturn(
                Optional.empty()
        );

        // when + then
        Assertions.assertThrows(UsernameNotFoundException.class, () -> customUserDetailService.loadUserByUsername("email"));
    }

    @Test
    @DisplayName("loadUserByUsername - 유저가 있음")
    void loadUserByUsername_2() {
        // given
        when(
                userRepository.findByEmail("email")
        ).thenReturn(
                Optional.of(User.builder().email("email").build())
        );

        // when
        UserDetails got = customUserDetailService.loadUserByUsername("email");

        // then
        Assertions.assertEquals("email", got.getUsername());
    }
}