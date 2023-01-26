package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.domain.entity.EmailVerifyCode;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import com.plogcareers.backend.ums.domain.entity.VerifiedEmail;
import com.plogcareers.backend.ums.exception.*;
import com.plogcareers.backend.ums.repository.EmailVerifyCodeRepository;
import com.plogcareers.backend.ums.repository.UserRepository;
import com.plogcareers.backend.ums.repository.UserRoleRepository;
import com.plogcareers.backend.ums.repository.VerifiedEmailRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerifyCodeRepository emailVerifyCodeRepository;
    private final JavaMailSender javaMailSender;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final BlogRepository blogRepository;

    // 회원가입
    public void join(UserJoinRequest request) {

        // 인증된 이메일 조회
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findById(request.getEmail()).orElseThrow(VerifyEmailNotFoundException::new);
        if (!verifiedEmail.getVerifyToken().equals(request.getVerifyToken())) {
            throw new VerifyTokenUnmatchedException();
        }


        // 사용자 생성
        User user = userRepository.save(request.toUserEntity(passwordEncoder));
        userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role("ROLE_USER")
                        .build()
        );

        if (blogRepository.existsByBlogName(request.getBlogName())) {
            throw new BlogNameDuplicatedException();
        }

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

        return UserLoginResponse.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .token(new Token(jwtTokenProvider.createToken(user.getUsername(), user.getRoles(), user.getId())))
                .build();
    }

    public Long getLoginedUserID(String token) throws UserNotFoundException {
        if (!StringUtils.hasText(token)) {
            return 0L;
        }
        return userRepository.findByEmail(jwtTokenProvider.getUserPk(token)).orElseThrow(UserNotFoundException::new).getId();
    }


    public void sendVerifyJoinEmail(SendVerifyJoinEmailRequest request) {
        // 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicatedException();
        }

        String verifyCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase(); // 6자리 랜덤 문자열 생성

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
        // TODO: 이메일 내용 html화 하고, 꾸미기
        simpleMailMessage.setText("인증번호는 " + verifyCode + " 입니다.");
        javaMailSender.send(simpleMailMessage);
    }

    public VerifyJoinEmailResponse verifyJoinEmail(VerifyJoinEmailRequest request) {
        EmailVerifyCode emailVerifyCode = emailVerifyCodeRepository.findById(request.getEmail()).orElseThrow(EmailVerifyCodeNotFoundException::new);

        if (!emailVerifyCode.getVerifyCode().equals(request.getVerifyCode())) {
            throw new EmailVerifyCodeUnmatchedException();
        }

        // 이메일 인증 코드 정보 삭제
        emailVerifyCodeRepository.delete(emailVerifyCode);

        String verifyToken = RandomStringUtils.randomAlphanumeric(20);
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

    public void sendFindPasswordEmail(SendFindPasswordEmailRequest request) {
        // 유저 유무 체크
        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new UserNotFoundException();
        }

        // 인증 코드 생성
        String verifyCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase(); // 6자리 랜덤 문자열 생성

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
        String verifyToken = RandomStringUtils.randomAlphanumeric(20);
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
    }
}

