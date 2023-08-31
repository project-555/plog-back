package com.plogcareers.backend.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    // Blog Domain
    ERR_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "포스트를 찾을 수 없습니다."),
    ERR_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    ERR_CATEGORY_DUPLICATED(HttpStatus.CONFLICT, "중복되는 카테고리가 이미 존재합니다."),
    ERR_CATEGORY_BLOG_MISMATCHED(HttpStatus.CONFLICT, "카테고리와 블로그가 일치하지 않습니다."),
    ERR_BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 블로그입니다."),
    ERR_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 태그입니다"),
    ERR_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 덧글입니다."),
    ERR_PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "답글을 남길 덧글이 존재하지 않습니다."),
    ERR_POSTING_COMMENT_UNMATCHED(HttpStatus.BAD_REQUEST, "덧글과 포스팅이 일치하지 않습니다."),
    ERR_INVALID_PARENT_EXIST(HttpStatus.BAD_REQUEST, "답글을 남기지 못하는 덧글입니다."),
    ERR_NO_PROPER_AUTHORITY(HttpStatus.UNAUTHORIZED, "적절한 권한이 없습니다."),
    ERR_POSTING_STAR_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 해당 포스팅에 스타를 남기셨습니다."),
    ERR_BLOG_POSTING_UNMATCHED(HttpStatus.BAD_REQUEST, "블로그에 해당 포스팅이 속하지 않습니다."),
    ERR_POSTING_STAR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스팅에 남긴 스타가 존재하지 않습니다."),
    ERR_BLOG_TAG_UNMATCHED(HttpStatus.BAD_REQUEST, "블로그에 해당 태그가 속하지 않습니다."),
    ERR_BLOG_TAG_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 해당 블로그에 같은 이름의 태그가 존재합니다."),
    // Parameter Validation
    ERR_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터가 유효하지 않습니다."),
    // UMS Domain
    ERR_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일이나, 비밀번호가 일치하지 않습니다."),
    ERR_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    ERR_EMAIL_VERIFY_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일 인증 코드가 존재하지 않습니다."),
    ERR_EMAIL_VERIFY_CODE_UNMATCHED(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
    ERR_VERIFY_TOKEN_UNMATCHED(HttpStatus.BAD_REQUEST, "인증 토큰이 일치하지 않습니다."),
    ERR_VERIFY_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일은 인증되지 않았거나, 만료되었습니다."),
    ERR_EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ERR_BLOG_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 블로그 이름입니다."),

    ERR_INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Home Domain
    ERR_SELF_SUBSCRIBE(HttpStatus.BAD_REQUEST, "본인의 블로그는 구독할 수 없습니다."),
    ERR_SUBSCRIBE_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 구독한 블로그입니다."),
    ERR_SUBSCRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "구독정보를 찾을 수 없습니다."),


    // COMMON
    ERR_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류입니다."),
    ERR_UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.code = this.name();
        this.message = message;
    }
}
