package com.plogcareers.backend.blog.exception;


public class PostingNotFoundException extends RuntimeException {
    public PostingNotFoundException(){
        super("게시물이 존재하지 않습니다.");
    }
}
