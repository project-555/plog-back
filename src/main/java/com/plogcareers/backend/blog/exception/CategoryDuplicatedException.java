package com.plogcareers.backend.blog.exception;


import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class CategoryDuplicatedException extends UserException {

    public CategoryDuplicatedException() {
        super(ErrorCode.ERR_CATEGORY_DUPLICATED);
    }
}
