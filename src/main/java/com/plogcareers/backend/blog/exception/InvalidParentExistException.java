package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class InvalidParentExistException extends UserException {
    public InvalidParentExistException() {
        super(ErrorCode.ERR_INVALID_PARENT_EXIST);
    }
}
