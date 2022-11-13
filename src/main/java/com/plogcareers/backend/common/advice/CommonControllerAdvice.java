package com.plogcareers.backend.common.advice;

import com.plogcareers.backend.blog.controller.BlogControllerAdvice;
import com.plogcareers.backend.common.domain.dto.ErrorResponse;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.common.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(BlogControllerAdvice.class);

    @ExceptionHandler(InvalidParamException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParamException(InvalidParamException e) {
        return ErrorResponse.toUserErrorResponseEntity(e.getErrorCode(), e.getErrors());
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return ErrorResponse.toUserErrorResponseEntity(e.getErrorCode());
    }
}
