package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.CreatePostingRequest;
import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import com.plogcareers.backend.blog.domain.dto.ListPostingTagResponse;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.service.PostingService;
import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.error.ErrorMapper;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final PostingService postingService;
    private final ErrorMapper errorMapper;
    // 포스팅 단건을 생성하는 메서드
    @PostMapping("/posting")
    public ResponseEntity<SResponse> createPosting(@RequestBody CreatePostingRequest postingRequest) throws TagNotFoundException {
        Long postingId = postingService.createPosting(postingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SDataResponse<>(postingId));
    }
    // 포스팅 단건을 조회하는 메서드
    @GetMapping("/posting/{id}")
    public ResponseEntity<SResponse> getPosting(@PathVariable Long id)  {
        try {
            GetPostingResponse posting = postingService.getPosting(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(posting));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.USER_NOT_FOUND));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
    // 포스팅 단건을 삭제하는 메서드
    @DeleteMapping("/posting/{id}")
    public ResponseEntity<SResponse> deletePosting(@PathVariable Long id) {
        try {
            postingService.deletePosting(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse(ErrorCode.POSTING_NOT_FOUND));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }


    @ApiOperation(value = "State 전체조회")
    @ApiResponses(
            @ApiResponse(code = 200,message = "정삭 동작 시")
    )
    @GetMapping("/posting/states")
    public ResponseEntity<SResponse> listStates(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(postingService.listStates()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMapper.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    // 포스팅 태그를 조회하는 메서드
    @GetMapping("/posting/{postingId}/tag")
    public ResponseEntity GetPostingTag(@PathVariable Long postingId) {
        try {
            ListPostingTagResponse listPostingTag = postingService.listPostingTag(postingId);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(listPostingTag));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse("POSTING_NOT_FOUND"));
        }
    }
}