package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.PostingDto;
import com.plogcareers.backend.blog.repository.PostingRepository;
import com.plogcareers.backend.blog.service.PostingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostingService postingService;

    // 글 쓰는 페이지
    @GetMapping("/posting")
    public String write() {
        return "post/posting";
    }

    // 글 쓴 뒤 POST 메서드로 DB에 저장
    @PostMapping("/posting")
    public String write(PostingDto postingDto){
        postingService.savePost(postingDto);
        return "redirect:/"; //글 목록으로 redirect 해주기
    }

    // 쓴 글을 수정하는 페이지
    @GetMapping("posting/edit")
    public String edit(@PathVariable Long id){
        PostingDto postingDto = postingService.getPost(id);

        return "post/update";
    }

    // 수정 내용을 DB에 저장
    @PutMapping("posting/eidt")
    public String update(PostingDto postingDto) {
        postingService.savePost(postingDto);
        return "redirect:/"; //글 목록으로 redirect 해주기
    }

    // 쓴 글을 삭제
    @DeleteMapping("")
    public String delete(@PathVariable Long id) {
        postingService.deletePost(id);
        return "redirect:/"; //글 목록으로 redirect 해주기
    }

}