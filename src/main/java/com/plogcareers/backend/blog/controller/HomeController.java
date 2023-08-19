package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.service.HomeService;
import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.constant.Auth;
import com.plogcareers.backend.ums.service.AuthService;
import io.swagger.annotations.*;
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
    private final AuthService authService;

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
        Long loginedUserID = authService.getLoginedUserID(token);
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
        Long loginedUserID = authService.getLoginedUserID(token);
        homeService.deleteSubscribe(subscribeID, loginedUserID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "구독 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListSubscribesResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    }
    )
    @GetMapping("/{userID}/subscribe")
    public ResponseEntity<SResponse> listSubscribes(@ApiParam(name = "userID", value = "유저 ID") @PathVariable Long userID) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(homeService.listSubscribes(userID)));
    }

    @ApiOperation(value = "홈 포스팅 리스트(구독)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상조회(data)", response = ListHomePostingsResponse.class),
            @ApiResponse(code = 299, message = "정상조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)}
    )
    @GetMapping("/following-postings")
    public ResponseEntity<SDataResponse<ListHomePostingsResponse>> listFollowingPostings(@ApiIgnore @RequestHeader(name = Auth.token, required = false) String token,
                                                                                         ListFollowingPostingsRequest request) {
        Long loginedUserID = authService.getLoginedUserID(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(homeService.listFollowingPostings(loginedUserID, request.getSearch(), request.getLastCursorID(), request.getPageSize())));
    }

    @ApiOperation(value = "홈 포스팅 리스트(최신)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상조회(data)", response = ListHomePostingsResponse.class),
            @ApiResponse(code = 299, message = "정상조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)}
    )
    @GetMapping("/recent-postings")
    public ResponseEntity<SDataResponse<ListHomePostingsResponse>> listRecentPostings(ListRecentPostingsRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(
                homeService.listRecentPostings(request.getSearch(), request.getLastCursorID(), request.getPageSize()))
        );
    }
}
