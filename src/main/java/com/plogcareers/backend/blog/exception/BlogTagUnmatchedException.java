package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class BlogTagUnmatchedException extends UserException {
    public BlogTagUnmatchedException() {
        super(ErrorCode.ERR_BLOG_TAG_UNMATCHED);
    }
}
