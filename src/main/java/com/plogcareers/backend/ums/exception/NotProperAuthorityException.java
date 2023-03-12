package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class NotProperAuthorityException extends UserException {
    public NotProperAuthorityException() {
        super(ErrorCode.ERR_NO_PROPER_AUTHORITY);
    }
}
