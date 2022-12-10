package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class PostingCommentUnmatchedException extends UserException {
    public PostingCommentUnmatchedException() {
        super(ErrorCode.ERR_POSTING_COMMENT_UNMATCHED);
    }
}
