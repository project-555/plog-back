package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.PostingDeleteRequest;
import com.plogcareers.backend.blog.domain.dto.PostingRequest;
import com.plogcareers.backend.blog.domain.dto.PostingResponse;
import com.plogcareers.backend.blog.service.PostingService;

import com.plogcareers.backend.common.domain.dto.SResponse;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostingService postingService;

    // 글 쓰는 페이지
//    @GetMapping("/posting")
//    public String write() {
//        return "post/posting";
//    }

    // 글 쓴 뒤 POST 메서드로 DB에 저장
    @PostMapping("/posting")
    public ResponseEntity<SResponse> write(@RequestBody PostingRequest postingRequest){
        postingService.savePost(postingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posting/{id}")
    public ResponseEntity<SResponse> read(@RequestParam Long id) {
        postingService.getPost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 쓴 글을 수정하는 페이지
//    @GetMapping("posting/edit")
//    public String edit(@PathVariable Long id){
//        PostingRequest postingDto = postingService.getPost(id);
//
//        return "post/update";
//    }

    // 수정 내용을 DB에 저장
//    @PutMapping("posting/edit")
//    public String update(PostingRequest postingDto) {
//        postingService.savePost(postingDto);
//        return "redirect:/"; //글 목록으로 redirect 해주기
//    }

    // 쓴 글을 삭제
    @DeleteMapping("posting/delete")
    public ResponseEntity<SResponse> delete(@RequestBody PostingDeleteRequest postingDeleteRequest) {
        postingService.deletePost(postingDeleteRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}