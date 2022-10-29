package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SErrorResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.error.ErrorMapper;
import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginResponse;
import com.plogcareers.backend.ums.exception.EmailDuplicatedException;
import com.plogcareers.backend.ums.exception.LoginFailException;
import com.plogcareers.backend.ums.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "회원가입")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "회원가입 정상처리"),
        @ApiResponse(code = 400, message = "회원가입 실패", response = SErrorResponse.class)
    })
    public ResponseEntity<SResponse> join(@RequestBody UserJoinRequest request) {
        try {
            userService.join(request);
        } catch (PSQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse(ErrorCode.EMAIL_ALREADY_EXIST));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이메일 확인
    @GetMapping("/email/chk")
    @ApiOperation(value = "이메일 중복확인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이메일 사용 가능", response = SResponse.class),
            @ApiResponse(code = 400, message = "이메일 중복", response = SErrorResponse.class)
    })
    public ResponseEntity<SResponse> emailCheck(@RequestParam String email) {
        try {
            userService.emailCheck(email);
        } catch (EmailDuplicatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse(ErrorCode.EMAIL_ALREADY_EXIST));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그인
    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    @ApiResponses(value = {
            @ApiResponse(code=200, message = "로그인 성공(data)", response = UserLoginResponse.class),
            @ApiResponse(code = 299, message = "로그인 성공(outer)", response = SDataResponse.class),
            @ApiResponse(code=400, message = "로그인 실패", response = SErrorResponse.class)
    })
    public ResponseEntity<SResponse> login(@RequestBody UserLoginRequest request) {
        try {
            var user = userService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(user));
        } catch (LoginFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse(ErrorCode.LOGIN_EMAIL_PW_UNMATCHED));
        }
    }
}