package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.CreatePostingRequest;
import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.service.BlogService;
import com.plogcareers.backend.blog.service.PostingService;

import com.plogcareers.backend.common.domain.dto.SDataResponse;
import com.plogcareers.backend.common.domain.dto.SResponse;
import com.plogcareers.backend.common.error.ErrorMapper;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;
    private final PostingService postingService;
    private final ErrorMapper errorMapper;
    // 포스팅 단건을 생성하는 메서드
    @PostMapping("/posting")
    public ResponseEntity<SResponse> CreatePosting(@RequestBody CreatePostingRequest postingRequest){
        postingService.createPosting(postingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // 포스팅 단건을 조회하는 메서드
    @GetMapping("/posting/{id}")
    public ResponseEntity<SResponse> GetPosting(@PathVariable Long id)  {
        try {
            GetPostingResponse posting = postingService.getPosting(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SDataResponse<>(posting));
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse("POSTING_NOT_FOUND"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse("USER_NOT_FOUND"));
        }
    }
    // 포스팅 단건을 삭제하는 메서드
    @DeleteMapping("/posting/{id}")
    public ResponseEntity<SResponse> DeletePosting(@PathVariable Long id) {
        try {
            postingService.deletePosting(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PostingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMapper.toErrorResponse("POSTING_NOT_FOUND"));
        }
    }
}