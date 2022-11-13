package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class TagNotFoundException extends UserException {
    public TagNotFoundException() {
        super(ErrorCode.ERR_TAG_NOT_FOUND);
    }
}
