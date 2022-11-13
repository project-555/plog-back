package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.service.BlogService;
import com.plogcareers.backend.blog.service.PostingService;
import com.plogcareers.backend.common.annotation.LogExecutionTime;
import com.plogcareers.backend.common.domain.dto.*;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.service.UserService;
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
@RequestMapping("/blog")
@Api(tags = "Blog Domain")
public class BlogController {

    private final BlogService blogService;
    private final PostingService postingService;
    private final UserService userService;

    @ApiOperation(value = "Posting 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정삭 동작 시"),
            @ApiResponse(code = 404, message = "태그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/posting")
    public ResponseEntity<SResponse> createPosting(@Valid @RequestBody CreatePostingRequest postingRequest, BindingResult result) throws TagNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long postingId = postingService.createPosting(postingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SDataResponse<>(postingId));

    }

    @ApiOperation(value = "Posting 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = GetPostingResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "포스팅 혹은 유저 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @GetMapping("/posting/{id}")
    @LogExecutionTime
    public ResponseEntity<SResponse> getPosting(@PathVariable Long id) throws UserNotFoundException {
        GetPostingResponse posting = postingService.getPosting(id);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(posting));

    }

    @ApiOperation(value = "Posting 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정삭 삭제"),
            @ApiResponse(code = 404, message = "포스팅 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/posting/{id}")
    public ResponseEntity<SResponse> deletePosting(@PathVariable Long id) {
        postingService.deletePosting(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @ApiOperation(value = "State 전체조회")
    @ApiResponses(
            @ApiResponse(code = 200, message = "정삭 동작 시")
    )
    @GetMapping("/posting/states")
    public ResponseEntity<SResponse> listStates() {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listStates()));
    }

    @ApiOperation(value = "Posting Tag 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListPostingTagResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "포스팅 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @GetMapping("/posting/{postingId}/tag")
    public ResponseEntity<SResponse> getPostingTag(@PathVariable Long postingId) {
        ListPostingTagResponse listPostingTag = postingService.listPostingTag(postingId);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(listPostingTag));
    }

    @ApiOperation(value = "Category 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정삭 동작 시"),
            @ApiResponse(code = 400, message = "중복된 카테고리가 존재함"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/category")
    public ResponseEntity<SResponse> createCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        blogService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Category 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListCategoryResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "블로그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @GetMapping("/{blogId}/categories")
    public ResponseEntity<SResponse> getCategory(@PathVariable Long blogId) {
        ListCategoryResponse listCategoryResponse = postingService.listCategory(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(listCategoryResponse));

    }
    @ApiOperation(value = "Category 리스트 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 수정(data)", response = ListCategoryResponse.class),
            @ApiResponse(code = 299, message = "정상 수정(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "블로그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @PutMapping("/{blogId}/categories")
    public ResponseEntity<SResponse> putCategory(@Valid @RequestBody UpdateCategoriesRequest updateCategoryRequest) {
        try {
            blogService.setCategory(updateCategoryRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.BLOG_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "포스팅 ID로 덧글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상조회 (data)", response = ListCommentsResponse.class),
            @ApiResponse(code = 299, message = "정상조회 (outer)", response = SOPagingResponse.class),
            @ApiResponse(code = 400, message = "잘못된 유저 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "해당하는 포스팅 ID를 가진 포스팅 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)}
    )
    @GetMapping("/posting/{postingId}/comments")
    public ResponseEntity<SResponse> listComments(@ApiIgnore @RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                  @ApiParam(name = "postingId", value = "포스팅 ID", required = true) @PathVariable Long postingId,
                                                  @Valid OPagingRequest request,
                                                  BindingResult result) throws UserNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserId = userService.getLoginedUserId(token);
        return ResponseEntity.status(HttpStatus.OK).body(postingService.listComments(loginedUserId, postingId, request));
    }
}