package com.plogcareers.backend.blog.controller;

import com.plogcareers.backend.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {


    @GetMapping("/hello")
    public String helloWorld(){
        return "Hello Blog!";
    }
}
