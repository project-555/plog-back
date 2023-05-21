package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class TagDuplicatedException extends UserException {
    public TagDuplicatedException() {
        super(ErrorCode.ERR_BLOG_TAG_DUPLICATED);
    }
}
