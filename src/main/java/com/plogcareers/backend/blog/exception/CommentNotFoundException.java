package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class CommentNotFoundException extends UserException {
    public CommentNotFoundException() {
        super(ErrorCode.ERR_COMMENT_NOT_FOUND);
    }
}
