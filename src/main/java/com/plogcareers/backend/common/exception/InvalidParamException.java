package com.plogcareers.backend.common.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InvalidParamException extends UserException {

    private final Errors errors;

    public InvalidParamException(Errors errors) {
        super(ErrorCode.ERR_LOGIN_FAILED);
        this.errors = errors;
    }
}
