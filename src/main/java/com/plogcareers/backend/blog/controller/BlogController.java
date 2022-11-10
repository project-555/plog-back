package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.CategoryDuplicatedException;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.service.BlogService;
import com.plogcareers.backend.blog.service.PostingService;
import com.plogcareers.backend.common.domain.dto.*;
import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.error.ErrorMapper;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
@Api(tags = "Blog Domain")
public class BlogController {

    private final BlogService blogService;
    private final PostingService postingService;
    private final ErrorMapper errorMapper;

    @ApiOperation(value = "Posting 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정삭 동작 시"),
            @ApiResponse(code = 404, message = "태그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/posting")
    public ResponseEntity<SResponse> createPosting(@Valid @RequestBody CreatePostingRequest postingRequest) {
        try {
            Long postingId = postingService.createPosting(postingRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SDataResponse<>(postingId));
        } catch (TagNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.TAG_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
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
    public ResponseEntity<SResponse> getPosting(@PathVariable Long id) {
        try {
            GetPostingResponse posting = postingService.getPosting(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(posting));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.USER_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
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
        try {
            postingService.deletePosting(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "State 전체조회")
    @ApiResponses(
            @ApiResponse(code = 200, message = "정삭 동작 시")
    )
    @GetMapping("/posting/states")
    public ResponseEntity<SResponse> listStates() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listStates()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
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
        try {
            ListPostingTagResponse listPostingTag = postingService.listPostingTag(postingId);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(listPostingTag));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
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
        try {
            blogService.createCategory(categoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.BLOG_NOT_FOUND));
        } catch (CategoryDuplicatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMapper.toErrorResponse(ErrorCode.CATEGORY_ALREADY_EXIST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
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
        try {
            ListCategoryResponse listCategoryResponse = postingService.listCategory(blogId);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(listCategoryResponse));
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.BLOG_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "포스팅 ID로 덧글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상조회 (data)", response = ListCommentResponse.class),
            @ApiResponse(code = 299, message = "정상조회 (outer)", response = SOPagingResponse.class),
            @ApiResponse(code = 400, message = "잘못된 유저 요청", response = SErrorResponse.class),
            @ApiResponse(code = 404, message = "해당하는 포스팅 ID를 가진 포스팅 없음", response = SErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = SErrorResponse.class)}
    )
    @GetMapping("/posting/{postingId}/comments")
    public ResponseEntity<SResponse> listComments(@PathVariable @Min(1) @ApiParam(value = "포스팅 ID", required = true, example = "1") Long postingId, OPagingRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postingService.listComments(postingId, request));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}