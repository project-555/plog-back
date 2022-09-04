package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import com.plogcareers.backend.ums.exception.LoginFailException;
import com.plogcareers.backend.ums.repository.UserRepository;
import com.plogcareers.backend.ums.repository.UserRoleRepository;
import com.plogcareers.backend.ums.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

    // 로그인
    public String login(@RequestBody UserLoginRequest request) {
        User member = userRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailException::new);
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LoginFailException();
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
