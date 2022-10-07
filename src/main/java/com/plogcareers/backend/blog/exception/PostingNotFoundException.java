package com.plogcareers.backend.blog.exception;

import org.postgresql.util.PSQLException;

public class PostingNotFoundException extends Exception {
    public PostingNotFoundException(){
        super("게시물이 존재하지 않습니다.");
    }
}
