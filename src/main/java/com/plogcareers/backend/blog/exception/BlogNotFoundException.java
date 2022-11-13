package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class BlogNotFoundException extends UserException {
    public BlogNotFoundException() {
        super(ErrorCode.ERR_BLOG_NOT_FOUND);
    }
}
