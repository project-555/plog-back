package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.dto.RefreshAccessTokenResponse;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.repository.postgres.BlogRepository;
import com.plogcareers.backend.ums.component.AuthorizationCodeCase;
import com.plogcareers.backend.ums.component.AuthorizationCodeGenerator;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.domain.entity.EmailVerifyCode;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import com.plogcareers.backend.ums.domain.entity.VerifiedEmail;
import com.plogcareers.backend.ums.exception.*;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import com.plogcareers.backend.ums.repository.postgres.UserRoleRepository;
import com.plogcareers.backend.ums.repository.redis.EmailVerifyCodeRepository;
import com.plogcareers.backend.ums.repository.redis.VerifiedEmailRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerifyCodeRepository emailVerifyCodeRepository;
    private final JavaMailSender javaMailSender;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final BlogRepository blogRepository;
    private final AuthorizationCodeGenerator authorizationCodeGenerator;

    // 회원가입
    @Transactional
    public void join(UserJoinRequest request) {
        // 인증된 이메일 조회
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findById(request.getEmail()).orElseThrow(VerifyEmailNotFoundException::new);
        if (!verifiedEmail.getVerifyToken().equals(request.getVerifyToken())) {
            throw new VerifyTokenUnmatchedException();
        }

        // 블로그명 중복 체크
        if (blogRepository.existsByBlogName(request.getBlogName())) {
            throw new BlogNameDuplicatedException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = userRepository.save(request.toUserEntity(encodedPassword));

        userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role("ROLE_USER")
                        .build()
        );

        // 사용자 블로그 생성
        blogRepository.save(request.toBlogEntity(user));

        // 회원 가입 후 레코드 삭제
        verifiedEmailRepository.delete(verifiedEmail);
    }


    // 로그인
    public UserLoginResponse login(@RequestBody UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(LoginFailException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailException();
        }

        List<Blog> blogs = blogRepository.findByUser(user);
        if (blogs.isEmpty()) {
            throw new BlogNotFoundException();
        }

        return UserLoginResponse.builder()
                .token(new Token(jwtTokenProvider.createToken(user.getUsername(), user, blogs.get(0).getId())))
                .build();
    }

    public RefreshAccessTokenResponse refreshAccessToken(Long loginedUserID) {
        User user = userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new);
        List<Blog> blogs = blogRepository.findByUser(user);
        if (blogs.isEmpty()) {
            throw new BlogNotFoundException();
        }

        return RefreshAccessTokenResponse.builder()
                .token(new Token(jwtTokenProvider.createToken(user.getUsername(), user, blogs.get(0).getId())))
                .build();
    }

    public Long getLoginedUserID(String token) throws UserNotFoundException {
        if (!StringUtils.hasText(token)) {
            return 0L;
        }
        return userRepository
                .findByEmail(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(token)))
                .orElseThrow(UserNotFoundException::new)
                .getId();
    }


    public void sendVerifyJoinEmail(SendVerifyJoinEmailRequest request) {
        // 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicatedException();
        }

        String verifyCode = authorizationCodeGenerator.generate(6, AuthorizationCodeCase.UPPER_CASE); // 6자리 랜덤 문자열 생성

        // Redis에 이메일 및 인증 코드 저장
        emailVerifyCodeRepository.save(
                EmailVerifyCode.builder()
                        .email(request.getEmail())
                        .expiredTime(300L) // 이메일 인증 코드는 5분간 유효
                        .verifyCode(verifyCode)
                        .build()
        );

        // 유저에게 인증 이메일 전송
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(request.getEmail());
        simpleMailMessage.setSubject("PlogCareers 회원가입 인증 메일입니다.");
        simpleMailMessage.setText("인증번호는 " + verifyCode + " 입니다."); // TODO: 이메일 내용 html화 하고, 꾸미기

        javaMailSender.send(simpleMailMessage);
    }

    public VerifyJoinEmailResponse verifyJoinEmail(VerifyJoinEmailRequest request) {
        EmailVerifyCode emailVerifyCode = emailVerifyCodeRepository.findById(request.getEmail()).orElseThrow(EmailVerifyCodeNotFoundException::new);

        if (!emailVerifyCode.getVerifyCode().equals(request.getVerifyCode())) {
            throw new EmailVerifyCodeUnmatchedException();
        }

        // 이메일 인증 코드 정보 삭제
        emailVerifyCodeRepository.delete(emailVerifyCode);

        String verifyToken = authorizationCodeGenerator.generate(20, AuthorizationCodeCase.NONE);
        // 인증된 이메일 정보 저장
        verifiedEmailRepository.save(
                VerifiedEmail.builder()
                        .email(request.getEmail())
                        .verifyToken(verifyToken)
                        .expiredTime(1200L) // 이메일 인증 정보는 20분간 유효
                        .build()
        );

        return VerifyJoinEmailResponse.builder()
                .email(request.getEmail())
                .verifyToken(verifyToken)
                .build();
    }

    public void sendVerifyFindPasswordEmail(SendVerifyFindPasswordEmailRequest request) {
        // 유저 유무 체크
        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new UserNotFoundException();
        }

        // 인증 코드 생성
        String verifyCode = authorizationCodeGenerator.generate(6, AuthorizationCodeCase.UPPER_CASE); // 6자리 대문자 랜덤 문자열 생성

        // Redis에 이메일 및 인증 코드 저장
        emailVerifyCodeRepository.save(
                EmailVerifyCode.builder()
                        .email(request.getEmail())
                        .expiredTime(300L) // 이메일 인증 코드는 5분간 유효
                        .verifyCode(verifyCode)
                        .build()
        );

        // 유저에게 인증 이메일 전송
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(request.getEmail());
        simpleMailMessage.setSubject("PlogCareers 비밀번호 찾기 인증 메일입니다.");
        // TODO: 이메일 내용 html화 하고, 꾸미기
        simpleMailMessage.setText("인증번호는 " + verifyCode + " 입니다.");
        javaMailSender.send(simpleMailMessage);
    }

    public VerifyFindPasswordEmailResponse verifyFindPasswordEmail(VerifyFindPasswordEmailRequest request) {
        // 이메일 인증 코드 확인
        EmailVerifyCode emailVerifyCode = emailVerifyCodeRepository.findById(request.getEmail()).orElseThrow(EmailVerifyCodeNotFoundException::new);
        if (!emailVerifyCode.getVerifyCode().equals(request.getVerifyCode())) {
            throw new EmailVerifyCodeUnmatchedException();
        }

        // 이메일 인증 코드 정보 삭제
        emailVerifyCodeRepository.delete(emailVerifyCode);

        // 비밀번호 변경 인증 토큰 생성
        String verifyToken = authorizationCodeGenerator.generate(20, AuthorizationCodeCase.NONE);
        // 비밀번호 변경 토큰 정보 저장
        verifiedEmailRepository.save(
                VerifiedEmail.builder()
                        .email(request.getEmail())
                        .expiredTime(1200L) // 인증 토큰은 20분간 유효
                        .verifyToken(verifyToken)
                        .build()
        );

        return VerifyFindPasswordEmailResponse.builder()
                .email(request.getEmail())
                .verifyToken(verifyToken)
                .build();
    }

    public void changePassword(ChangePasswordRequest request) {
        // 비밀번호 변경 인증 토큰 확인
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findById(request.getEmail()).orElseThrow(VerifyEmailNotFoundException::new);
        if (!verifiedEmail.getVerifyToken().equals(request.getVerifyToken())) {
            throw new VerifyTokenUnmatchedException();
        }

        // 비밀번호 변경
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // 비밀번호 변경 인증 토큰 정보 삭제
        verifiedEmailRepository.delete(verifiedEmail);
    }

    public void updateUserProfile(Long loginedUserID, UpdateUserProfileRequest request) {
        User user = userRepository.findById(request.getUserID()).orElseThrow(UserNotFoundException::new);
        if (!loginedUserID.equals(user.getId())) {
            throw new NotProperAuthorityException();
        }
        userRepository.save(request.toUserEntity(user));
    }

    public void updateUserPassword(Long loginedUserID, UpdateUserPasswordRequest request) {
        User user = userRepository.findById(request.getUserID()).orElseThrow(UserNotFoundException::new);
        if (!loginedUserID.equals(user.getId())) {
            throw new NotProperAuthorityException();
        }
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        userRepository.save(request.toUserEntity(user, passwordEncoder));
    }

    public void exitUser(Long loginedUserID, ExitUserRequest request) throws UserNotFoundException {
        User user = userRepository.findById(request.getUserID()).orElseThrow(UserNotFoundException::new);
        if (!loginedUserID.equals(user.getId())) {
            throw new NotProperAuthorityException();
        }
        userRepository.delete(user);
    }
}

