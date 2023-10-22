package com.plogcareers.backend.common.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plogcareers.backend.common.error.ErrorCode;
import com.plogcareers.backend.common.exception.InvalidParamException;
import com.plogcareers.backend.common.exception.UserException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.HashMap;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@Component
public class ErrorLogFormatter {
    private final ObjectMapper mapper;

    public String json(Throwable exception) {
        HashMap<String, Object> map = new HashMap<>();
        if (exception instanceof InvalidParamException) {
            ErrorCode errorCode = ((InvalidParamException) exception).getErrorCode();
            Errors errors = ((InvalidParamException) exception).getErrors();

            map.put("code", errorCode.getCode());
            map.put("message", errorCode.getMessage());
            map.put("detail", errors);
        } else if (exception instanceof UserException) {
            ErrorCode errorCode = ((UserException) exception).getErrorCode();
            map.put("code", errorCode.getCode());
            map.put("message", errorCode.getMessage());
        } else {
            map.put("code", "INTERNAL_ERROR");
            // stacktrace는 근접한 5개의 스택만 보여준다.
            StackTraceElement[] stacktrace = Arrays.stream(exception.getStackTrace()).toList().subList(0, 5).toArray(StackTraceElement[]::new);
            map.put("stacktrace", stacktrace);
        }
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return String.format("\"%s\"", "JsonProcessingException"); // 발생 가능성 없음.
        }

    }
}
