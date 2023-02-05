package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.CreateSubscribeRequest;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.service.HomeService;
import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.constant.Auth;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Api(tags = "Home Domain")
public class HomeController {

    private final HomeService homeService;
    private final UserService userService;

    @ApiOperation(value = "블로그 구독")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 동작 시"),
            @ApiResponse(code = 400, message = "이미 구독한 블로그", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "블로그 혹은 유저 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/subscribes")
    public ResponseEntity<SResponse> createSubscribe(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                     @Valid @RequestBody CreateSubscribeRequest request,
                                                     BindingResult result) throws BlogNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = userService.getLoginedUserID(token);
        homeService.createSubscribe(loginedUserID, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "블로그 구독 취소")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 동작 시"),
            @ApiResponse(code = 400, message = "본인 블로그 구독 불가", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "블로그 혹은 유저 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/subscribes/{subscribeID}")
    public ResponseEntity<SResponse> deleteSubscribe(@PathVariable Long subscribeID,
                                                     @ApiIgnore @RequestHeader(name = Auth.token) String token
    ) throws BlogNotFoundException {
        Long loginedUserID = userService.getLoginedUserID(token);
        homeService.deleteSubscribe(subscribeID, loginedUserID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
