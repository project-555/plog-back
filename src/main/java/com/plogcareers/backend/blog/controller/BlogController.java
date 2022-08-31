package com.plogcareers.backend.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BlogController {
    @GetMapping("/")
    public String helloWorld(){
        return "Hello Spring!";
    }
}
