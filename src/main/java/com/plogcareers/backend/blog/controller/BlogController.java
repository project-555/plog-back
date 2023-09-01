package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.validator.PatchBlogRequestValidator;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.service.BlogService;
import com.plogcareers.backend.blog.service.PostingService;
import com.plogcareers.backend.common.annotation.LogExecutionTime;
import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.ums.constant.Auth;
import com.plogcareers.backend.ums.domain.dto.CheckBlogNameExistRequest;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
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
@RequestMapping("/blogs")
@Api(tags = "Blog Domain")
public class BlogController {

    private final BlogService blogService;
    private final PostingService postingService;
    private final AuthService authService;
    private final PatchBlogRequestValidator patchBlogRequestValidator;

    @ApiOperation(value = "Posting 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 동작 시"),
            @ApiResponse(code = 404, message = "태그 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{blogID}/postings")
    public ResponseEntity<SResponse> createPosting(@ApiParam(name = "blogID", value = "블로그 ID", required = true) @PathVariable Long blogID,
                                                   @ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                   @Valid @RequestBody CreatePostingRequest request,
                                                   BindingResult result) throws TagNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        Long postingID = postingService.createPosting(blogID, loginedUserID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SDataResponse<>(postingID));
    }

    @ApiOperation(value = "Posting 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = GetPostingResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "포스팅 혹은 유저 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @GetMapping("/{blogID}/postings/{postingID}")
    @LogExecutionTime
    public ResponseEntity<SResponse> getPosting(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                @ApiParam(name = "postingID", value = "포스팅 ID") @PathVariable Long postingID,
                                                @ApiIgnore @RequestHeader(name = Auth.token, required = false) String token) throws UserNotFoundException {
        Long loginedUserID = authService.getLoginedUserID(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.getPosting(blogID, postingID, loginedUserID)));

    }

    @ApiOperation(value = "Posting 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListPostingsResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping("/{blogID}/postings")
    public ResponseEntity<SResponse> listPostings(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                  @ApiIgnore @RequestHeader(name = Auth.token, required = false) String token,
                                                  @Valid ListPostingsRequest request,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listPostings(blogID, loginedUserID, request)));
    }

    @ApiOperation(value = "Posting 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 수정"),
            @ApiResponse(code = 400, message = "잘못된 파라미터 요청", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "수정할 권한이 없음", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "포스팅이 없거나, 블로그가 없거나, 유저 정보가 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @PutMapping("/{blogID}/postings/{postingID}")
    public ResponseEntity<SResponse> updatePosting(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                   @PathVariable Long blogID,
                                                   @PathVariable Long postingID,
                                                   @Valid @RequestBody UpdatePostingRequest request,
                                                   BindingResult result) throws BlogNotFoundException, UserNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.updatePosting(loginedUserID, blogID, postingID, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Posting 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정삭 삭제"),
            @ApiResponse(code = 404, message = "포스팅 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{blogID}/postings/{postingID}")
    public ResponseEntity<SResponse> deletePosting(@PathVariable Long blogID, @PathVariable Long postingID,
                                                   @ApiIgnore @RequestHeader(name = Auth.token) String token) {
        Long userID = authService.getLoginedUserID(token);
        postingService.deletePosting(blogID, postingID, userID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @ApiOperation("Posting 카운트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = CountPostingsResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "자원 없음. (상세 내용은 에러 메시지 참조)", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping("/{blogID}/postings/count")
    public ResponseEntity<SResponse> countPosting(@PathVariable Long blogID, CountPostingsRequest request) {
        request.setBlogID(blogID);

        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.countPostings(request)));
    }

    @ApiOperation(value = "State 전체조회")
    @ApiResponses(
            @ApiResponse(code = 200, message = "정삭 동작 시")
    )
    @GetMapping("/states")
    public ResponseEntity<SResponse> listStates() {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listStates()));
    }

    @ApiOperation(value = "Posting Tag 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListPostingTagResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "포스팅 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    }
    )
    @GetMapping("/{blogID}/postings/{postingID}/tags")
    public ResponseEntity<SResponse> listPostingTags(@PathVariable Long blogID, @PathVariable Long postingID) {
        ListPostingTagResponse listPostingTag = postingService.listPostingTags(blogID, postingID);
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
    @PostMapping("/{blogID}/categories")
    public ResponseEntity<SResponse> createCategory(@PathVariable Long blogID, @ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                    @Valid @RequestBody CreateCategoryRequest categoryRequest) throws UserNotFoundException {
        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.createCategory(blogID, loginedUserID, categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Category 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListCategoriesResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "블로그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @GetMapping("/{blogID}/categories")
    public ResponseEntity<SResponse> listCategories(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(blogService.listCategories(blogID)));

    }

    @ApiOperation(value = "Category 리스트 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 수정(data)", response = ListCategoriesResponse.class),
            @ApiResponse(code = 299, message = "정상 수정(outer)", response = SDataResponse.class),
            @ApiResponse(code = 404, message = "블로그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @PutMapping("/{blogID}/categories/{categoryID}")
    public ResponseEntity<SResponse> updateCategory(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                    @ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                    @ApiParam(name = "categoryID", value = "카테고리 ID") @PathVariable Long categoryID,
                                                    @Valid @RequestBody UpdateCategoryRequest request,
                                                    BindingResult result) throws BlogNotFoundException, UserNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.updateCategory(blogID, loginedUserID, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Category 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정삭 삭제"),
            @ApiResponse(code = 404, message = "블로그 없음"),
            @ApiResponse(code = 500, message = "서버 에러")
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{blogID}/categories/{categoryID}")
    public ResponseEntity<SResponse> deleteCategory(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                    @ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                    @ApiParam(name = "categoryID", value = "카테고리 ID") @PathVariable Long categoryID) throws UserNotFoundException {
        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.deleteCategory(blogID, categoryID, loginedUserID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ApiOperation(value = "포스팅 ID로 덧글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상조회 (data)", response = ListCommentsResponse.class),
            @ApiResponse(code = 299, message = "정상조회 (outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "잘못된 유저 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "해당하는 포스팅 ID를 가진 포스팅 없음", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)}
    )
    @GetMapping("/{blogID}/postings/{postingID}/comments")
    public ResponseEntity<SResponse> listComments(@ApiIgnore @RequestHeader(name = Auth.token, required = false) String token,
                                                  @ApiParam(name = "blogID", value = "블로그 ID", required = true) @PathVariable Long blogID,
                                                  @ApiParam(name = "postingID", value = "포스팅 ID", required = true) @PathVariable Long postingID) throws UserNotFoundException {
        Long loginedUserID = authService.getLoginedUserID(token);
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listComments(blogID, postingID, loginedUserID)));
    }

    @ApiOperation(value = "포스팅에 덧글 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "정상 등록"),
                    @ApiResponse(code = 404, message = "포스팅이 없거나, 로그인한 유저 정보가 없음.", response = ErrorResponse.class),
                    @ApiResponse(code = 400, message = "잘못된 파라미터 요청", response = ErrorResponse.class)
            }
    )
    @PostMapping("/{blogID}/postings/{postingID}/comment")
    public ResponseEntity<SResponse> createComment(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                   @ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                   @ApiParam(name = "postingID", value = "포스팅 ID") @PathVariable Long postingID,
                                                   @Valid @RequestBody CreateCommentRequest request,
                                                   BindingResult result) throws PostingNotFoundException, UserNotFoundException {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.createComment(blogID, postingID, loginedUserID, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "포스팅 덧글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "정상 수정"),
                    @ApiResponse(code = 400, message = "잘못된 파라미터 요청", response = ErrorResponse.class),
                    @ApiResponse(code = 401, message = "수정할 권한이 없음", response = ErrorResponse.class),
                    @ApiResponse(code = 404, message = "포스팅이 없거나, 덧글이 없거나, 유저 정보가 없음", response = ErrorResponse.class),
            }
    )
    @PutMapping("/{blogID}/postings/{postingID}/comments/{commentID}")
    public ResponseEntity<SResponse> updateComment(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                   @ApiParam(name = "blogID", value = "블로그 ID", required = true) @PathVariable Long blogID,
                                                   @ApiParam(name = "postingID", value = "포스팅 ID", required = true) @PathVariable Long postingID,
                                                   @ApiParam(name = "commentID", value = "덧글 ID", required = true) @PathVariable Long commentID,
                                                   @Valid @RequestBody UpdateCommentRequest request,
                                                   BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.updateComment(blogID, postingID, commentID, loginedUserID, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "포스팅 덧글 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 삭제"),
            @ApiResponse(code = 401, message = "삭제할 권한이 없음 (덧글 작성자거나, 포스팅 작성자가 아님)", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "포스팅이 없거나, 덧글이 없거나, 유저장보가 없음", response = ErrorResponse.class),
    })
    @DeleteMapping("/{blogID}/postings/{postingID}/comments/{commentID}")
    public ResponseEntity<SResponse> deleteComment(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                   @ApiParam(name = "blogID", value = "블로그 ID", required = true) @PathVariable Long blogID,
                                                   @ApiParam(name = "postingID", value = "포스팅 ID", required = true) @PathVariable Long postingID,
                                                   @ApiParam(name = "commentID", value = "덧글 ID", required = true) @PathVariable Long commentID) {
        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.deleteComment(blogID, postingID, commentID, loginedUserID);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "특정 포스팅 스타 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회(data)", response = ListPostingStarsResponse.class),
            @ApiResponse(code = 299, message = "정상 조회(outer)", response = SDataResponse.class),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "블로그가 없거나, 포스팅이 존재하지 않음", response = ErrorResponse.class)
    })
    @GetMapping("/{blogID}/postings/{postingID}/stars")
    public ResponseEntity<SResponse> listPostingStars(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                      @ApiParam(name = "postingID", value = "포스팅 ID") @PathVariable Long postingID) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listPostingStars(blogID, postingID)));
    }

    @ApiOperation(value = "특정 포스팅에 스타 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 생성"),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @PostMapping("/{blogID}/postings/{postingID}/star")
    public ResponseEntity<SResponse> createPostingStar(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                       @ApiParam(name = "postingID", value = "포스팅 ID") @PathVariable Long postingID,
                                                       @ApiIgnore @RequestHeader(name = Auth.token) String token) {
        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.createPostingStar(blogID, postingID, loginedUserID);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "특정 포스팅에 스타 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 삭제"),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청"),
            @ApiResponse(code = 404, message = "자원 없음")
    })
    @DeleteMapping("/{blogID}/postings/{postingID}/star")
    public ResponseEntity<SResponse> deletePostingStar(@ApiParam(name = "blogID", value = "블로그 ID") @PathVariable Long blogID,
                                                       @ApiParam(name = "postingID", value = "포스팅 ID") @PathVariable Long postingID,
                                                       @ApiIgnore @RequestHeader(name = Auth.token) String token) {

        Long loginedUserID = authService.getLoginedUserID(token);
        postingService.deletePostingStar(blogID, postingID, loginedUserID);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "태그 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회", response = ListTagsResponse.class),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @GetMapping("/{blogID}/tags")
    public ResponseEntity<SResponse> listTags(@PathVariable Long blogID) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(blogService.listTags(blogID)));
    }

    @ApiOperation(value = "태그 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상 생성"),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @PostMapping("/{blogID}/tags")
    public ResponseEntity<SResponse> createTag(@PathVariable Long blogID,
                                               @ApiIgnore @RequestHeader(name = Auth.token) String token,
                                               @Valid @RequestBody CreateTagRequest request,
                                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SDataResponse<>(
                        blogService.createTag(blogID, loginedUserID, request)
                )
        );
    }

    @ApiOperation(value = "태그 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 수정"),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @PutMapping("/{blogID}/tags/{tagID}")
    public ResponseEntity<SResponse> updateTag(@PathVariable Long blogID,
                                               @PathVariable Long tagID,
                                               @ApiIgnore @RequestHeader(name = Auth.token) String token,
                                               @Valid @RequestBody UpdateTagRequest request,
                                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.updateTag(blogID, tagID, loginedUserID, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "태그 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 삭제"),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @DeleteMapping("/{blogID}/tags/{tagID}")
    public ResponseEntity<SResponse> deleteTag(@ApiParam(value = "삭제하고자 하는 태그가 속해 있는 블로그 ID", example = "1") @PathVariable Long blogID,
                                               @ApiParam(value = "삭제하고자 하는 태그 ID", example = "10") @PathVariable Long tagID,
                                               @ApiIgnore @RequestHeader(name = Auth.token) String token) {
        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.deleteTag(blogID, tagID, loginedUserID);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "블로그명 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "블로그명 중복 없음, 사용 가능"),
            @ApiResponse(code = 400, message = "블로그명 중복 있음, 사용 불가", response = ErrorResponse.class)
    })
    @PostMapping("/check-blog-name-exist")
    public ResponseEntity<SResponse> checkBlogNameExist(@Valid @RequestBody CheckBlogNameExistRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }
        blogService.checkBlogNameExist(request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "블로그 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상 조회", response = GetBlogResponse.class),
            @ApiResponse(code = 400, message = "잘못된 사용자 요청", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "자원 없음", response = ErrorResponse.class)
    })
    @GetMapping("/{blogID}")
    public ResponseEntity<SResponse> getBlog(@ApiParam(value = "조회하고자 하는 블로그의 ID", example = "1") @PathVariable Long blogID) {
        return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(blogService.getBlog(blogID)));
    }

    @ApiOperation(value = "블로그 소개 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "정상 수정"),
            @ApiResponse(code = 401, message = "잘못된 사용자 요청", response = ErrorResponse.class),
    })
    @PatchMapping("/{blogID}")
    public ResponseEntity<SResponse> patchBlog(@ApiIgnore @RequestHeader(name = Auth.token) String token,
                                                       @PathVariable Long blogID,
                                                       @RequestBody PatchBlogRequest request,
                                                       BindingResult result) {
        patchBlogRequestValidator.validate(request, result);

        if (result.hasErrors()) {
            throw new InvalidParamException(result);
        }

        Long loginedUserID = authService.getLoginedUserID(token);
        blogService.patchBlog(loginedUserID, blogID, request);

        return ResponseEntity.noContent().build();
    }

}