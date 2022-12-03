package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;
import lombok.Getter;

@Getter
public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(ErrorCode.ERR_USER_NOT_FOUND);
    }
}
