package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class PostingStarNotFoundException extends UserException {
    public PostingStarNotFoundException() {
        super(ErrorCode.ERR_POSTING_STAR_NOT_FOUND);
    }
}
