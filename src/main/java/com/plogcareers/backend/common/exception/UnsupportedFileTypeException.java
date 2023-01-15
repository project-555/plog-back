package com.plogcareers.backend.common.exception;

import com.plogcareers.backend.common.error.ErrorCode;

public class UnsupportedFileTypeException extends UserException {
    public UnsupportedFileTypeException() {
        super(ErrorCode.ERR_UNSUPPORTED_FILE_TYPE);
    }
}
