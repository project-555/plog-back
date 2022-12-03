package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class CommentPostingMismatchedException extends UserException {
    public CommentPostingMismatchedException() {
        super(ErrorCode.ERR_COMMENT_POSTING_MISMATCHED);
    }
}
