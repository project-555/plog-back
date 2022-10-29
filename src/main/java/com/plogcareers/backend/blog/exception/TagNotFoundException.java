package com.plogcareers.backend.blog.exception;

public class TagNotFoundException extends Exception {
    public TagNotFoundException(){
        super("태그가 존재하지 않습니다.");
    }
}
