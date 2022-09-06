package com.plogcareers.backend.ums.exception;

import lombok.Getter;
import org.postgresql.util.PSQLException;

@Getter
public class EmailDuplicatedException extends Exception {
    public EmailDuplicatedException() {
        super("중복되는 이메일이 존재합니다.");
    }
}
