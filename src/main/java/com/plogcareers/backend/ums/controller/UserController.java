package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.domain.dto.EmailCheckRequest;
import com.plogcareers.backend.ums.domain.dto.UserJoinRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginRequest;
import com.plogcareers.backend.ums.domain.dto.UserLoginResponse;
import com.plogcareers.backend.ums.exception.EmailDuplicatedException;
import com.plogcareers.backend.ums.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Api(tags = "UMS Domain")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원가입 정상처리"),
            @ApiResponse(code = 400, message = "회원가입 실패", response = ErrorResponse.class)
    })
    public ResponseEntity<SResponse> join(@RequestBody @Valid UserJoinRequest request) {
        userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이메일 확인
    @GetMapping("/email/chk")
    @ApiOperation(value = "이메일 중복확인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이메일 사용 가능", response = SResponse.class),
            @ApiResponse(code = 400, message = "이메일 중복", response = ErrorResponse.class)
    })
    public ResponseEntity<SResponse> emailCheck(@Valid EmailCheckRequest request, BindingResult result) throws InvalidParamException, EmailDuplicatedException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        userService.emailCheck(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그인
    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공(data)", response = UserLoginResponse.class),
            @ApiResponse(code = 299, message = "로그인 성공(outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "로그인 실패", response = ErrorResponse.class)
    })
    public ResponseEntity<SResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(userService.login(request)));
    }
}