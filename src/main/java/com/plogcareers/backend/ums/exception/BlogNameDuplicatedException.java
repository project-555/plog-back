package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class BlogNameDuplicatedException extends UserException {
    public BlogNameDuplicatedException() {
        super(ErrorCode.ERR_BLOG_NAME_DUPLICATED);
    }
}
