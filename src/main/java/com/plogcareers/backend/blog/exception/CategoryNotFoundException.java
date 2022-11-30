package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class CategoryNotFoundException extends UserException {
    public CategoryNotFoundException() {
        super(ErrorCode.ERR_CATEGORY_NOT_FOUND);
    }
}
