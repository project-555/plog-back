package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginResponse;
import com.plogcareers.backend.ums.exception.EmailDuplicatedException;
import com.plogcareers.backend.ums.exception.LoginFailException;
import com.plogcareers.backend.ums.service.UserService;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest request) {
        try {
            userService.join(request);
        } catch (PSQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패하였습니다.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공하였습니다.");
    }

    // 이메일 확인
    @GetMapping("/email/chk")
    public ResponseEntity<String> emailCheck(@RequestParam String email) {
        try{
            userService.emailCheck(email);
        } catch (EmailDuplicatedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일입니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        try {
            var response = userService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (LoginFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}