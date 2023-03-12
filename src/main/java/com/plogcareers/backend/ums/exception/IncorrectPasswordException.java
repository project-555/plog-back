package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class IncorrectPasswordException extends UserException {
    public IncorrectPasswordException() {
        super(ErrorCode.ERR_INCORRECT_PASSWORD);
    }
}
