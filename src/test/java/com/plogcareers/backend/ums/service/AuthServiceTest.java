package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.dto.RefreshAccessTokenResponse;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.repository.postgres.BlogRepository;
import com.plogcareers.backend.ums.component.AuthorizationCodeCase;
import com.plogcareers.backend.ums.component.AuthorizationCodeGenerator;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.domain.entity.*;
import com.plogcareers.backend.ums.exception.*;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import com.plogcareers.backend.ums.repository.postgres.UserRoleRepository;
import com.plogcareers.backend.ums.repository.redis.EmailVerifyCodeRepository;
import com.plogcareers.backend.ums.repository.redis.VerifiedEmailRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    BlogRepository blogRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    AuthService authService;

    @Mock
    VerifiedEmailRepository verifiedEmailRepository;

    @Mock
    EmailVerifyCodeRepository emailVerifyCodeRepository;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    AuthorizationCodeGenerator authorizationCodeGenerator;

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

    @Test
    @DisplayName("join - verifiedEmail이 없는 경우")
    void join_1() {
        // given
        when(
                verifiedEmailRepository.findById("test@test.com")
        ).thenThrow(VerifyEmailNotFoundException.class);

        // when + then
        Assertions.assertThrows(
                VerifyEmailNotFoundException.class, () -> authService.join(
                        UserJoinRequest.builder()
                                .email("test@test.com")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("join verifyToken이 다른 경우")
    void join_2() {
        // given
        when(
                verifiedEmailRepository.findById("test@test.com")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("1")
                        .email("test@test.com")
                        .build())
        );

        // when + then
        Assertions.assertThrows(
                VerifyTokenUnmatchedException.class, () -> authService.join(
                        UserJoinRequest.builder()
                                .email("test@test.com")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("join - 사용자가 지정한 블로그 명이 이미 있는 경우")
    void join_3() {
        // given
        when(
                verifiedEmailRepository.findById("test@test.com")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("1")
                        .email("test@test.com")
                        .build())
        );

        when(
                blogRepository.existsByBlogName("testBlog")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(
                BlogNameDuplicatedException.class, () -> authService.join(
                        UserJoinRequest.builder()
                                .email("test@test.com")
                                .verifyToken("1")
                                .blogName("testBlog")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("join - 정상동작")
    void join_4() {
        // given
        when(
                verifiedEmailRepository.findById("test@test.com")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("1")
                        .email("test@test.com")
                        .build())
        );

        when(
                blogRepository.existsByBlogName("test_blog")
        ).thenReturn(false);

        when(
                userRepository.save(any())
        ).thenReturn(
                User.builder()
                        .id(1L)
                        .build()
        );

        when(
                passwordEncoder.encode("test_password")
        ).thenReturn("encoded_password");

        when(
                userRoleRepository.save(
                        UserRole.builder()
                                .user(any())
                                .role("ROLE_USER")
                                .build()
                )
        ).thenReturn(
                UserRole.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .role("ROLE_USER")
                        .build()
        );

        // when
        authService.join(
                UserJoinRequest.builder()
                        .verifyToken("1")
                        .email("test@test.com")
                        .firstName("test_first_name")
                        .lastName("test_last_name")
                        .nickname("test_nickname")
                        .password("test_password")
                        .blogName("test_blog")
                        .sex(Sex.MALE)
                        .build()
        );

        // then
        verify(userRepository, times(1)).save(
                User.builder()
                        .joinDt(any())
                        .email("test@test.com")
                        .firstName("test_first_name")
                        .lastName("test_last_name")
                        .nickname("test_nickname")
                        .sex("MALE")
                        .password("encoded_password")
                        .roles(List.of(UserRole.builder().role("ROLE_USER").build()))
                        .build());
        verify(verifiedEmailRepository, times(1)).delete(VerifiedEmail.builder()
                .verifyToken("1")
                .email("test@test.com")
                .build());
    }

    @Test
    @DisplayName("login - 이메일이 없는 경우")
    void login_1() {
        // given
        when(
                userRepository.findByEmail("test_email")
        ).thenThrow(LoginFailException.class);

        // when + then
        Assertions.assertThrows(
                LoginFailException.class, () -> authService.login(
                        UserLoginRequest.builder()
                                .email("test_email")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("login - 비밀번호가 다른 경우")
    void login_2() {
        // given
        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(
                Optional.of(User.builder()
                        .password("encoded_password")
                        .build()
                )
        );

        when(
                passwordEncoder.matches("test_password", "encoded_password")
        ).thenReturn(false);

        // when + then
        Assertions.assertThrows(
                LoginFailException.class, () -> authService.login(
                        UserLoginRequest.builder()
                                .email("test_email")
                                .password("test_password")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("login - 유저가 생성한 블로그가 없는 경우")
    void login_3() {
        // given
        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(
                Optional.of(User.builder()
                        .password("encoded_password")
                        .build()
                )
        );

        when(
                passwordEncoder.matches("test_password", "encoded_password")
        ).thenReturn(true);

        when(
                blogRepository.findByUser(any())
        ).thenReturn(List.of());

        // when + then
        Assertions.assertThrows(
                BlogNotFoundException.class, () -> authService.login(
                        UserLoginRequest.builder()
                                .email("test_email")
                                .password("test_password")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("login - 정상동작")
    void login_4() {
        // given
        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(
                Optional.of(
                        User.builder()
                                .email("test_email")
                                .password("encoded_password")
                                .build()
                )
        );

        when(
                passwordEncoder.matches("test_password", "encoded_password")
        ).thenReturn(true);

        when(
                blogRepository.findByUser(any())
        ).thenReturn(List.of(Blog.builder().id(1L).build()));

        when(
                jwtTokenProvider.createToken("test_email", User.builder()
                        .email("test_email")
                        .password("encoded_password")
                        .build(), 1L)
        ).thenReturn("test_token");

        // when
        UserLoginResponse got = authService.login(
                UserLoginRequest.builder()
                        .email("test_email")
                        .password("test_password")
                        .build()
        );

        // then
        Assertions.assertEquals(
                UserLoginResponse.builder()
                        .token(new Token("test_token"))
                        .build(), got);
    }

    @Test
    @DisplayName("refreshAccessToken - 유저를 찾지 못함")
    void refreshAccessToken_1() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(Optional.empty());

        // when + then
        Assertions.assertThrows(
                UserNotFoundException.class, () -> authService.refreshAccessToken(1L)
        );
    }

    @Test
    @DisplayName("refreshAccessToken - 유저의 블로그를 찾지 못함")
    void refreshAccessToken_2() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(
                blogRepository.findByUser(User.builder().id(1L).build())
        ).thenReturn(List.of());

        // when + then
        Assertions.assertThrows(
                BlogNotFoundException.class, () -> authService.refreshAccessToken(1L)
        );
    }

    @Test
    @DisplayName("refreshAccessToken - 정상동작")
    void refreshAccessToken_3() {
        // given
        when(
                userRepository.findById(1L)
        ).thenReturn(
                Optional.of(User.builder()
                        .id(1L)
                        .email("test_email")
                        .password("encoded_password")
                        .build()
                )
        );

        when(
                blogRepository.findByUser(User.builder()
                        .id(1L)
                        .email("test_email")
                        .password("encoded_password")
                        .build())
        ).thenReturn(List.of(Blog.builder().id(1L).build()));

        when(
                jwtTokenProvider.createToken("test_email",
                        User.builder()
                                .id(1L)
                                .email("test_email")
                                .password("encoded_password")
                                .build(), 1L)
        ).thenReturn("test_token");

        // when
        RefreshAccessTokenResponse got = authService.refreshAccessToken(1L);

        // then
        Assertions.assertEquals(
                RefreshAccessTokenResponse.builder()
                        .token(new Token("test_token"))
                        .build(), got);
    }

    @Test
    @DisplayName("getLoginedUserID - token이 없는 경우 0 리턴")
    void getLoginedUserID_1() {
        // when
        Long got = authService.getLoginedUserID("");

        // then
        Assertions.assertEquals(0L, got);
    }

    @Test
    @DisplayName("getLoginedUserID - 이메일로 찾은 결과가 없는 경우")
    void getLoginedUserID_2() {
        // given
        when(
                jwtTokenProvider.resolveToken("test_token")
        ).thenReturn(
                "resolved_token"
        );
        when(
                jwtTokenProvider.getUserPk("resolved_token")
        ).thenReturn("test_email");
        when(
                userRepository.findByEmail("test_email")
        ).thenThrow(UserNotFoundException.class);

        // when + then
        Assertions.assertThrows(
                UserNotFoundException.class, () -> authService.getLoginedUserID("test_token")
        );
    }

    @Test
    @DisplayName("getLoginedUserID - 정상동작")
    void getLoginedUserID_3() {
        // given
        when(
                jwtTokenProvider.resolveToken("test_token")
        ).thenReturn(
                "resolved_token"
        );
        when(
                jwtTokenProvider.getUserPk("resolved_token")
        ).thenReturn("test_email");
        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(
                Optional.of(User.builder().id(1L).build())
        );

        // when
        Long got = authService.getLoginedUserID("test_token");

        // then
        Assertions.assertEquals(1L, got);
    }

    @Test
    @DisplayName("sendVerifyJoinEmail - 인증 대상 이메일이 중복인 경우")
    void sendVerifyJoinEmail_1() {
        // given
        when(
                userRepository.existsByEmail("test_email")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(
                EmailDuplicatedException.class, () -> authService.sendVerifyJoinEmail(SendVerifyJoinEmailRequest.builder().email("test_email").build())
        );
    }

    @Test
    @DisplayName("sendVerifyJoinEmail - 정상 동작 시")
    void sendVerifyJoinEmail_2() {
        // given
        when(
                userRepository.existsByEmail("test_email")
        ).thenReturn(false);

        when(
                authorizationCodeGenerator.generate(6, AuthorizationCodeCase.UPPER_CASE)
        ).thenReturn(
                "TESTAA"
        );

        // when
        authService.sendVerifyJoinEmail(SendVerifyJoinEmailRequest.builder().email("test_email").build());


        verify(emailVerifyCodeRepository, times(1)).save(EmailVerifyCode.builder()
                .email("test_email")
                .expiredTime(300L)
                .verifyCode("TESTAA")
                .build()
        );


        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("test_email");
        simpleMailMessage.setSubject("PlogCareers 회원가입 인증 메일입니다.");
        simpleMailMessage.setText("인증번호는 TESTAA 입니다.");

        verify(javaMailSender, times(1)).send(simpleMailMessage);
    }

    @Test
    @DisplayName("verifyJoinEmail - 인증 코드가 없는 경우")
    void verifyJoinEmail_1() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenThrow(EmailVerifyCodeNotFoundException.class);

        // when + then
        Assertions.assertThrows(
                EmailVerifyCodeNotFoundException.class, () -> authService.verifyJoinEmail(VerifyJoinEmailRequest.builder().email("test_email").verifyCode("test_code").build())
        );
    }

    @Test
    @DisplayName("verifyJoinEmail - 인증 코드가 다른 경우")
    void verifyJoinEmail_2() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenReturn(
                Optional.of(EmailVerifyCode.builder()
                        .email("test_email")
                        .verifyCode("test_code")
                        .build()
                )
        );

        // when + then
        Assertions.assertThrows(
                EmailVerifyCodeUnmatchedException.class, () -> authService.verifyJoinEmail(VerifyJoinEmailRequest.builder().email("test_email").verifyCode("test_code2").build())
        );
    }

    @Test
    @DisplayName("verifyJoinEmail - 정상동작")
    void verifyJoinEmail_3() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenReturn(
                Optional.of(EmailVerifyCode.builder()
                        .email("test_email")
                        .verifyCode("test_code")
                        .build()
                )
        );
        when(
                authorizationCodeGenerator.generate(20, AuthorizationCodeCase.NONE)
        ).thenReturn(
                "test_verify_token"
        );

        // when
        VerifyJoinEmailResponse got = authService.verifyJoinEmail(VerifyJoinEmailRequest.builder().email("test_email").verifyCode("test_code").build());

        // then
        VerifyJoinEmailResponse want = VerifyJoinEmailResponse.builder()
                .email("test_email")
                .verifyToken("test_verify_token")
                .build();

        Assertions.assertEquals(want, got);

        verify(emailVerifyCodeRepository, times(1)).delete(EmailVerifyCode.builder()
                .email("test_email")
                .verifyCode("test_code")
                .build()
        );
    }

    @Test
    @DisplayName("sendVerifyFindPasswordEmail - 인증 대상 이메일이 없는 경우")
    void sendVerifyFindPasswordEmail_1() {
        // given
        when(
                userRepository.existsByEmail("test_email")
        ).thenReturn(false);

        // when + then
        Assertions.assertThrows(
                UserNotFoundException.class, () -> authService.sendVerifyFindPasswordEmail(SendVerifyFindPasswordEmailRequest.builder().email("test_email").build())
        );
    }

    @Test
    @DisplayName("sendVerifyFindPasswordEmail - 정상 동작 시")
    void sendVerifyFindPasswordEmail_2() {
        // given
        when(
                userRepository.existsByEmail("test_email")
        ).thenReturn(true);

        when(
                authorizationCodeGenerator.generate(6, AuthorizationCodeCase.UPPER_CASE)
        ).thenReturn(
                "TESTAA"
        );


        // when
        authService.sendVerifyFindPasswordEmail(SendVerifyFindPasswordEmailRequest.builder().email("test_email").build());

        // then
        verify(emailVerifyCodeRepository, times(1)).save(EmailVerifyCode.builder()
                .email("test_email")
                .expiredTime(300L)
                .verifyCode("TESTAA")
                .build()
        );

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("test_email");
        simpleMailMessage.setSubject("PlogCareers 비밀번호 찾기 인증 메일입니다.");
        simpleMailMessage.setText("인증번호는 TESTAA 입니다.");

        verify(javaMailSender, times(1)).send(simpleMailMessage);
    }

    @Test
    @DisplayName("verifyFindPasswordEmail - 인증 코드가 없는 경우")
    void verifyFindPasswordEmail_1() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenThrow(EmailVerifyCodeNotFoundException.class);

        // when + then
        Assertions.assertThrows(
                EmailVerifyCodeNotFoundException.class, () -> authService.verifyFindPasswordEmail(
                        VerifyFindPasswordEmailRequest.builder()
                                .email("test_email")
                                .verifyCode("test_code")
                                .build())
        );
    }

    @Test
    @DisplayName("verifyFindPasswordEmail - 인증 코드가 다른 경우")
    void verifyFindPasswordEmail_2() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenReturn(
                Optional.of(EmailVerifyCode.builder()
                        .email("test_email")
                        .verifyCode("test_code")
                        .build()
                )
        );

        // when + then
        Assertions.assertThrows(
                EmailVerifyCodeUnmatchedException.class, () -> authService.verifyFindPasswordEmail(
                        VerifyFindPasswordEmailRequest.builder()
                                .email("test_email")
                                .verifyCode("test_code2")
                                .build())
        );
    }

    @Test
    @DisplayName("verifyFindPasswordEmail - 정상동작")
    void verifyFindPasswordEmail_3() {
        // given
        when(
                emailVerifyCodeRepository.findById("test_email")
        ).thenReturn(
                Optional.of(EmailVerifyCode.builder()
                        .email("test_email")
                        .verifyCode("test_code")
                        .build()
                )
        );
        when(
                authorizationCodeGenerator.generate(20, AuthorizationCodeCase.NONE)
        ).thenReturn(
                "test_verify_token"
        );

        // when
        VerifyFindPasswordEmailResponse got = authService.verifyFindPasswordEmail(
                VerifyFindPasswordEmailRequest.builder()
                        .email("test_email")
                        .verifyCode("test_code")
                        .build()
        );

        // then
        VerifyFindPasswordEmailResponse want = VerifyFindPasswordEmailResponse.builder()
                .email("test_email")
                .verifyToken("test_verify_token")
                .build();

        Assertions.assertEquals(want, got);
    }

    @Test
    @DisplayName("changePassword - 인증 토큰이 없는 경우")
    void changePassword_1() {
        // given
        when(
                verifiedEmailRepository.findById("test_email")
        ).thenThrow(VerifyEmailNotFoundException.class);

        // when + then
        Assertions.assertThrows(
                VerifyEmailNotFoundException.class, () -> authService.changePassword(
                        ChangePasswordRequest.builder()
                                .verifyToken("test_token")
                                .email("test_email")
                                .password("test_password")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("changePassword - 인증 토큰이 요청과 다름")
    void changePassword_2() {
        // given
        when(
                verifiedEmailRepository.findById("test_email")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("test_token")
                        .email("test_email")
                        .build()
                )
        );

        // when + then
        Assertions.assertThrows(
                VerifyTokenUnmatchedException.class, () -> authService.changePassword(
                        ChangePasswordRequest.builder()
                                .verifyToken("test_token2")
                                .email("test_email")
                                .password("test_password")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("changePassword - 유저가 없음")
    void changePassword_3() {
        // given
        when(
                verifiedEmailRepository.findById("test_email")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("test_token")
                        .email("test_email")
                        .build()
                )
        );

        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(Optional.empty());

        // when + then
        Assertions.assertThrows(
                UserNotFoundException.class, () -> authService.changePassword(
                        ChangePasswordRequest.builder()
                                .verifyToken("test_token")
                                .email("test_email")
                                .password("test_password")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("changePassword - 정상동작")
    void changePassword_4() {
        // given
        when(
                verifiedEmailRepository.findById("test_email")
        ).thenReturn(
                Optional.of(VerifiedEmail.builder()
                        .verifyToken("test_token")
                        .email("test_email")
                        .build()
                )
        );

        when(
                userRepository.findByEmail("test_email")
        ).thenReturn(
                Optional.of(User.builder()
                        .email("test_email")
                        .password("test_password")
                        .build()
                )
        );

        when(
                passwordEncoder.encode("test_password")
        ).thenReturn("encoded_password");

        // when
        authService.changePassword(
                ChangePasswordRequest.builder()
                        .verifyToken("test_token")
                        .email("test_email")
                        .password("test_password")
                        .build()
        );

        // then
        verify(userRepository, times(1)).save(
                User.builder()
                        .email("test_email")
                        .password("encoded_password")
                        .build()
        );

        verify(verifiedEmailRepository, times(1)).delete(
                VerifiedEmail.builder()
                        .verifyToken("test_token")
                        .email("test_email")
                        .build()
        );
    }
}