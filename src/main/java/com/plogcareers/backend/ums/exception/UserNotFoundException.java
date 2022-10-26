package com.plogcareers.backend.ums.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("정보가 일치하는 유저가 없습니다.");
    }
}
