package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class BlogPostingUnmatchedException extends UserException {
    public BlogPostingUnmatchedException() {
        super(ErrorCode.ERR_BLOG_POSTING_UNMATCHED);
    }
}
