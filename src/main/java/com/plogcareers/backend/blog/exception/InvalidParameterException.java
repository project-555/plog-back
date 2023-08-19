package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class InvalidParameterException extends UserException {
    public InvalidParameterException() {
        super(ErrorCode.ERR_INVALID_PARAMETER);
    }
}
