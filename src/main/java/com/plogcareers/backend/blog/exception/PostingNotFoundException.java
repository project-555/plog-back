package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class PostingNotFoundException extends UserException {
    public PostingNotFoundException() {
        super(ErrorCode.ERR_POST_NOT_FOUND);
    }
}
