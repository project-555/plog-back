package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class VerifyTokenUnmatchedException extends UserException {

    public VerifyTokenUnmatchedException() {
        super(ErrorCode.ERR_VERIFY_TOKEN_UNMATCHED);
    }
}
