package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class SubscribeNotFoundException extends UserException {
    public SubscribeNotFoundException() {
        super(ErrorCode.ERR_SUBSCRIBE_NOT_FOUND);
    }
}
