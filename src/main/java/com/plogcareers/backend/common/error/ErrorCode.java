package com.plogcareers.backend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Blog Domain
    ERR_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_POST_NOT_FOUND", "포스트를 찾을 수 없습니다."),
    ERR_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_CATEGORY_NOT_FOUND", "카테고리를 찾을 수 없습니다."),
    ERR_CATEGORY_DUPLICATED(HttpStatus.BAD_REQUEST, "ERR_CATEGORY_DUPLICATED", "중복되는 카테고리가 이미 존재합니다."),
    ERR_BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_BLOG_NOT_FOUND", "존재하지 않는 블로그입니다."),
    ERR_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_TAG_NOT_FOUND", "존재하지 않는 태그입니다"),

    // Parameter Validation
    ERR_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "ERR_INVALID_PARAMETER", "파라미터가 유효하지 않습니다."),

    // UMS Domain
    ERR_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "ERR_LOGIN_FAILED", "이메일이나, 비밀번호가 일치하지 않습니다."),
    ERR_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_USER_NOT_FOUND", "유저가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;


}
