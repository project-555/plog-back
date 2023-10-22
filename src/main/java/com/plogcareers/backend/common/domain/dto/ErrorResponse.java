package com.plogcareers.backend.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plogcareers.backend.common.error.ErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ErrorResponse {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @ApiModelProperty(value = "예외 메시지")
    private String message; //예외 메시지 저장

    @ApiModelProperty(value = "예외 코드")
    private String code; // 예외를 세분화하기 위한 사용자 지정 코드,

    @ApiModelProperty(value = "예외 상태 HTTP 코드")
    private HttpStatus status; // HTTP 상태 값 저장 400, 404, 500 등..

    //@Valid의 Parameter 검증을 통과하지 못한 필드가 담긴다.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private List<UserFieldError> userFieldErrors;

    public static ResponseEntity<ErrorResponse> toUserErrorResponseEntity(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    public static ResponseEntity<ErrorResponse> toUserErrorResponseEntity(ErrorCode errorCode, Errors errors) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setUserFieldErrors(errors.getFieldErrors());

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    public static ResponseEntity<ErrorResponse> toInternalErrorResponseEntity() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(ErrorCode.ERR_INTERNAL_ERROR.getCode())
                .message(ErrorCode.ERR_INTERNAL_ERROR.getMessage())
                .build();

        errorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //BindingResult.getFieldErrors() 메소드를 통해 전달받은 fieldErrors
    public void setUserFieldErrors(List<FieldError> fieldErrors) {
        userFieldErrors = fieldErrors.stream().map(fieldError -> new UserFieldError(
                Objects.requireNonNull(fieldError.getCodes())[0],
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
        )).toList();
    }

    //parameter 검증에 통과하지 못한 필드가 담긴 클래스.
    public record UserFieldError(String field, Object value, String reason) {
    }
}
