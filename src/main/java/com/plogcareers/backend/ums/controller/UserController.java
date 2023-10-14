package com.plogcareers.backend.ums.controller;

import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.ums.domain.dto.GetUserResponse;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Api(tags = "User Domain")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원 기본 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회", response = GetUserResponse.class),
            @ApiResponse(code = 404, message = "포스팅 혹은 유저 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping("/{userID}")
    public ResponseEntity<GetUserResponse> getUser(@ApiParam(name = "userID", value = "유저 ID") @PathVariable Long userID) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userID));
    }
}