package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.PostingRequest;
import com.plogcareers.backend.blog.service.PostingService;

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
    @GetMapping("/posting")
    public String write() {
        return "post/posting";
    }

    // 글 쓴 뒤 POST 메서드로 DB에 저장
    @PostMapping("/posting")
    public ResponseEntity<String> write(@RequestBody PostingRequest postingRequest){
        postingService.savePost(postingRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body("글 저장에 성공하였습니다.");
    }

    // 쓴 글을 수정하는 페이지
    @GetMapping("posting/edit")
    public String edit(@PathVariable Long id){
        PostingRequest postingDto = postingService.getPost(id);

        return "post/update";
    }

    // 수정 내용을 DB에 저장
    @PutMapping("posting/edit")
    public String update(PostingRequest postingDto) {
        postingService.savePost(postingDto);
        return "redirect:/"; //글 목록으로 redirect 해주기
    }

    // 쓴 글을 삭제
    @DeleteMapping("posting/delete")
    public String delete(@PathVariable Long id) {
        postingService.deletePost(id);
        return "redirect:/"; //글 목록으로 redirect 해주기
    }

}