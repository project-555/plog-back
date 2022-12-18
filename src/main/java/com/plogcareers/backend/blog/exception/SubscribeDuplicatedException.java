package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class SubscribeDuplicatedException extends UserException {

    public SubscribeDuplicatedException() {
        super(ErrorCode.ERR_SUBSCRIBE_DUPLICATED);
    }
}
