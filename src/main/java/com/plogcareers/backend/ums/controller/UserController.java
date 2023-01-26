package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SResponse> join(@Valid @RequestBody UserJoinRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @ApiOperation(value = "회원가입 이메일 인증 메일 전송")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "인증 메일 전송 성공")
    })
    @PostMapping("/send-verify-join-email")
    public ResponseEntity<SResponse> sendVerifyJoinEmail(@Valid @RequestBody SendVerifyJoinEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        userService.sendVerifyJoinEmail(request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "회원가입 이메일 인증")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이메일 인증 성공", response = VerifyJoinEmailResponse.class),
            @ApiResponse(code = 400, message = "이메일 인증 실패", response = ErrorResponse.class)
    })
    @PostMapping("/verify-join-email")
    public ResponseEntity<SResponse> verifyJoinEmail(@Valid @RequestBody VerifyJoinEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        VerifyJoinEmailResponse response = userService.verifyJoinEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(response));
    }

    @ApiOperation(value = "비밀번호 찾기 인증 메일 전송")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "비밀번호 찾기 이메일 전송 성공"),
            @ApiResponse(code = 400, message = "비밀번호 찾기 이메일 전송 실패", response = ErrorResponse.class)
    })
    @PostMapping("/send-find-password-email")
    public ResponseEntity<SResponse> sendFindPasswordEmail(@Valid @RequestBody SendFindPasswordEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        userService.sendFindPasswordEmail(request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "비밀번호 찾기 이메일 인증")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "비밀번호 찾기 이메일 인증 성공", response = VerifyFindPasswordEmailResponse.class),
            @ApiResponse(code = 400, message = "비밀번호 찾기 이메일 인증 실패", response = ErrorResponse.class)
    })
    @PostMapping("/verify-find-password-email")
    public ResponseEntity<SResponse> verifyFindPasswordEmail(@Valid @RequestBody VerifyFindPasswordEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        VerifyFindPasswordEmailResponse response = userService.verifyFindPasswordEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(response));
    }

    @ApiOperation(value = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "비밀번호 변경 성공"),
            @ApiResponse(code = 400, message = "비밀번호 변경 실패", response = ErrorResponse.class)
    })
    @PostMapping("/change-password")
    public ResponseEntity<SResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}