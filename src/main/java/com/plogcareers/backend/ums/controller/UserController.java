package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.error.ErrorMapper;
import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.exception.EmailDuplicatedException;
import com.plogcareers.backend.ums.exception.LoginFailException;
import com.plogcareers.backend.ums.service.UserService;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final ErrorMapper errorMapper;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<SResponse> join(@RequestBody UserJoinRequest request) {
        try {
            userService.join(request);
        } catch (PSQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse("EMAIL_ALREADY_EXIST"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이메일 확인
    @GetMapping("/email/chk")
    public ResponseEntity<SResponse> emailCheck(@RequestParam String email) {
        try {
            userService.emailCheck(email);
        } catch (EmailDuplicatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse("EMAIL_ALREADY_EXIST"));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<SResponse> login(@RequestBody UserLoginRequest request) {
        try {
            var user = userService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(user));
        } catch (LoginFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse("LOGIN_EMAIL_PW_UNMATCHED"));
        }
    }
}