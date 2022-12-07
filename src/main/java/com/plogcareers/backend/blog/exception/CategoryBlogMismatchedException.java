package com.plogcareers.backend.blog.exception;

import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.UserException;

public class CategoryBlogMismatchedException extends UserException {
    public CategoryBlogMismatchedException() {
        super(ErrorCode.ERR_CATEGORY_BLOG_MISMATCHED);
    }
}
