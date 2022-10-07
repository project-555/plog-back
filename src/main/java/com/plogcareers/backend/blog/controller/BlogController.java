package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.PostingDetailRequest;
import com.plogcareers.backend.blog.domain.dto.PostingUpdateRequest;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.service.BlogService;
import com.plogcareers.backend.blog.service.PostingService;

import com.plogcareers.backend.common.domain.dto.SResponse;
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

    // 글 쓴 뒤 POST 메서드로 DB에 저장
    @PostMapping("/posting")
    public ResponseEntity<SResponse> CreatePosting(@RequestBody PostingDetailRequest postingRequest){
        postingService.savePost(postingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // 게시글, 덧글, 태그, 카테고리
    @GetMapping("/posting/{id}")
    public ResponseEntity<SResponse> ReadPosting(@PathVariable Long id) throws PostingNotFoundException {
        postingService.getPost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{blogId}/postings")
    public ResponseEntity<SResponse> ListPosting(@PathVariable Long blogId){
        return ResponseEntity.status(200).build();
    }


    // 수정 내용을 DB에 저장
//    @PutMapping("/posting/{id}")
//    public ResponseEntity<SResponse> UpdatePosting(@PathVariable Long id, @RequestBody PostingUpdateRequest postingUpdateRequest) {
//        postingService.updatePost(id, postingUpdateRequest);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    // 쓴 글을 삭제
    @DeleteMapping("/posting/{id}")
    public ResponseEntity<SResponse> DeletePosting(@PathVariable Long id) {
        postingService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}