package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class EmailVerifyCodeUnmatchedException extends UserException {
    public EmailVerifyCodeUnmatchedException() {
        super(ErrorCode.ERR_EMAIL_VERIFY_CODE_UNMATCHED);
    }
}
