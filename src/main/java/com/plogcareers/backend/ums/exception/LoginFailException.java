package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class LoginFailException extends UserException {
    public LoginFailException() {
        super(ErrorCode.ERR_LOGIN_FAILED);
    }
}
