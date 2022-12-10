package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class PostingStarDuplicatedException extends UserException {

    public PostingStarDuplicatedException() {
        super(ErrorCode.ERR_POSTING_STAR_DUPLICATED);
    }
}
