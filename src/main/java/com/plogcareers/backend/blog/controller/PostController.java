package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.domain.dto.PostingDto;
import com.plogcareers.backend.blog.repository.PostingRepository;
import com.plogcareers.backend.blog.service.PostingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostingRepository postingRepository;
    private PostingService postingService;

    public PostController(PostingService postingService){
        this.postingService = postingService;
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello Post!";
    }

    @PostMapping("/")
    public String write(PostingDto postingDto){
        postingService.savePost(postingDto);
        return "redirect:/";
    }
}