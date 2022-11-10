package com.plogcareers.backend.blog.exception;


public class CategoryDuplicatedException extends RuntimeException {
    public CategoryDuplicatedException() {
        super("중복된 카테고리가 존재합니다.");
    }
}
