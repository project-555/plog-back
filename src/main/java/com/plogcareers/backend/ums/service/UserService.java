package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.ums.domain.dto.Token;
import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginResponse;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import com.plogcareers.backend.ums.exception.EmailDuplicatedException;
import com.plogcareers.backend.ums.exception.LoginFailException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import com.plogcareers.backend.ums.repository.UserRoleRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public void join(UserJoinRequest request) throws PSQLException {
        User user = userRepository.save(request.toEntity(passwordEncoder));
        userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role("ROLE_USER")
                        .build()
        );
    }

    // 이메일 중복확인
    public void emailCheck(String email) throws EmailDuplicatedException {
        Optional<User> opUser = userRepository.findByEmail(email);
        if (opUser.isPresent()) {
            throw new EmailDuplicatedException();
        }
    }

    // 로그인
    public UserLoginResponse login(@RequestBody UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailException();
        }

        return UserLoginResponse.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .token(new Token(jwtTokenProvider.createToken(user.getUsername(), user.getRoles(), user.getId())))
                .build();
    }

    public Long getLoginedUserId(String token) throws UserNotFoundException {
        return userRepository.findByEmail(jwtTokenProvider.getUserPk(token)).orElseThrow(UserNotFoundException::new).getId();
    }
}
