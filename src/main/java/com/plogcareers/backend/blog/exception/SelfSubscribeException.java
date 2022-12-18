package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class SelfSubscribeException extends UserException {

    public SelfSubscribeException() {
        super(ErrorCode.ERR_SELF_SUBSCRIBE);
    }
}