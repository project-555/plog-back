package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class VerifyEmailNotFoundException extends UserException {
    public VerifyEmailNotFoundException() {
        super(ErrorCode.ERR_VERIFY_EMAIL_NOT_FOUND);
    }
}
