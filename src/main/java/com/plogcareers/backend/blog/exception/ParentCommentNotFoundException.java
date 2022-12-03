package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class ParentCommentNotFoundException extends UserException {

    public ParentCommentNotFoundException() {
        super(ErrorCode.ERR_PARENT_COMMENT_NOT_FOUND);
    }
}
