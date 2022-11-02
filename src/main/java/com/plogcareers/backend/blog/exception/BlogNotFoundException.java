package com.plogcareers.backend.blog.exception;


public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(){
        super("블로그가 존재하지 않습니다.");
    }
}
