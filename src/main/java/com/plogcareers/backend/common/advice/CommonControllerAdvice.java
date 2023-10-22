package com.plogcareers.backend.common.advice;

import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.common.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParamException(InvalidParamException e) {
        return ErrorResponse.toUserErrorResponseEntity(ErrorCode.ERR_INVALID_PARAMETER, e.getErrors());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return ErrorResponse.toUserErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ErrorResponse.toInternalErrorResponseEntity();
    }
}
