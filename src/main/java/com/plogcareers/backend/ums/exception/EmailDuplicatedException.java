package com.plogcareers.backend.ums.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;
import lombok.Getter;

@Getter
public class EmailDuplicatedException extends UserException {
    public EmailDuplicatedException() {
        super(ErrorCode.ERR_EMAIL_DUPLICATED);
    }
}
