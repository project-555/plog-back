package com.plogcareers.backend.post.controller;

import com.plogcareers.backend.post.dto.PostDto;
import com.plogcareers.backend.post.repository.PostRepository;
import com.plogcareers.backend.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostRepository postRepository;
    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello Post!";
    }

    @PostMapping("/")
    public String write(PostDto postDto){
        postService.savePost(postDto);
        return "redirect:/";
    }
}
