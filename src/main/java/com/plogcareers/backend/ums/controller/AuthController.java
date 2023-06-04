package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.blog.domain.dto.RefreshAccessTokenResponse;
import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.constant.Auth;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Api(tags = "UMS Domain")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원가입 정상처리"),
            @ApiResponse(code = 400, message = "회원가입 실패", response = ErrorResponse.class)
    })
    public ResponseEntity<SResponse> join(@Valid @RequestBody UserJoinRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        authService.join(request);
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
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(authService.login(request)));
    }

    @ApiOperation(value = "회원가입 이메일 인증 메일 전송")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "인증 메일 전송 성공"),
            @ApiResponse(code = 400, message = "사용자 요청 오류", response = ErrorResponse.class)
    })
    @PostMapping("/send-verify-join-email")
    public ResponseEntity<SResponse> sendVerifyJoinEmail(@Valid @RequestBody SendVerifyJoinEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        authService.sendVerifyJoinEmail(request);
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
        VerifyJoinEmailResponse response = authService.verifyJoinEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(response));
    }

    @ApiOperation(value = "비밀번호 찾기 인증 메일 전송")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "비밀번호 찾기 이메일 전송 성공"),
            @ApiResponse(code = 400, message = "비밀번호 찾기 이메일 전송 실패", response = ErrorResponse.class)
    })
    @PostMapping("/send-verify-find-password-email")
    public ResponseEntity<SResponse> sendVerifyFindPasswordEmail(@Valid @RequestBody SendVerifyFindPasswordEmailRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        authService.sendVerifyFindPasswordEmail(request);
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
        VerifyFindPasswordEmailResponse response = authService.verifyFindPasswordEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(response));
    }

    @ApiOperation(value = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "비밀번호 변경 성공"),
            @ApiResponse(code = 400, message = "비밀번호 변경 실패", response = ErrorResponse.class)
    })
    @PostMapping("/change-password")
    public ResponseEntity<SResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(value = "회원 기본 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 수정"),
            @ApiResponse(code = 401, message = "잘못된 사용자 요청", response = ErrorResponse.class),
    })

    @PutMapping("/edit-profile")
    public ResponseEntity<SResponse> updateUserProfile(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                       @Valid @RequestBody UpdateUserProfileRequest request,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        authService.updateUserProfile(loginedUserID, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "회원 비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 수정"),
            @ApiResponse(code = 400, message = "기존 비밀번호 불일치", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "잘못된 사용자 요청", response = ErrorResponse.class)
    })

    @PutMapping("/edit-password")
    public ResponseEntity<SResponse> updateUserPassword(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                        @Valid @RequestBody UpdateUserPasswordRequest request,
                                                        BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        authService.updateUserPassword(loginedUserID, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 동작 시"),
            @ApiResponse(code = 404, message = "블로그 혹은 유저 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/exit-user")
    public ResponseEntity<SResponse> exitUser(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                              @Valid @RequestBody ExitUserRequest request) {
        Long loginedUserID = authService.getLoginedUserID(token);
        authService.exitUser(loginedUserID, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "엑세스 토큰 리프레시")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 리프레시 시"),
            @ApiResponse(code = 401, message = "잘못된 사용자 요청", response = ErrorResponse.class)
    })
    @PostMapping("/refresh-access-token")
    public ResponseEntity<SResponse> refreshToken(@ApiIgnore @RequestHeader(name = Auth.token) String token) {
        Long loginedUserID = authService.getLoginedUserID(token);
        RefreshAccessTokenResponse newToken = authService.refreshAccessToken(loginedUserID);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(newToken));
    }
}