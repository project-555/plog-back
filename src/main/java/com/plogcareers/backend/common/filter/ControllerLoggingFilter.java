package com.plogcareers.backend.common.filter;


import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class ControllerLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingFilter.class);
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // 응답에 따른 에러 로그 레벨 지정
    private void logByStatus(int status) {
        switch (status / 100) {
            case 2:
                logger.info("success");
                break;
            case 3:
                logger.info("redirect");
                break;
            case 4:
                logger.warn("client error");
                break;
            case 5:
                logger.error("internal server error");
                break;
            default:
                break;
        }
    }

    // 요청 ID 조회
    private String getRequestID(HttpServletRequest request) {
        return request.getHeader("requestID");
    }

    // 요청 핸들러 조회
    private String getCaller(HandlerExecutionChain handlerExecutionChain) {
        if (handlerExecutionChain != null) {
            Object handler = handlerExecutionChain.getHandler();
            if (handler instanceof HandlerMethod handlerMethod) {
                return String.format("%s.%s", handlerMethod.getBeanType().getSimpleName(), handlerMethod.getMethod().getName());
            }
        }

        return "";
    }

    // 요청 본문 조회
    private String getRequestBody(ContentCachingRequestWrapper request) {
        String requestBody = new String(request.getContentAsByteArray());
        return StringUtil.isNullOrEmpty(requestBody) ? "null" : requestBody;
    }

    // 응답 본문 조회
    private String getResponseBody(ContentCachingResponseWrapper response) {
        String responseBody = new String(response.getContentAsByteArray());
        return StringUtil.isNullOrEmpty(responseBody) ? "null" : responseBody;
    }

    private String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private String getStatus(HttpServletResponse response) {
        return String.valueOf(response.getStatus());
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 요청 핸들러 조회
        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);

        // 응답, 요청 본문 캐싱
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        // 필터 체인 수행
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);

        // 로깅 필드 지정
        // FIXME: 로그 필드를 동적으로 설정하기
        MDC.put("caller", getCaller(handlerExecutionChain));
        MDC.put("requestID", getRequestID(request));
        MDC.put("requestURL", getRequestURL(request));
        MDC.put("requestBody", getRequestBody(cachingRequestWrapper));
        MDC.put("responseBody", getResponseBody(cachingResponseWrapper));
        MDC.put("status", getStatus(cachingResponseWrapper));

        // 응답에 따른 에러 로그 레벨 지정
        logByStatus(cachingResponseWrapper.getStatus());

        // 로그 필드 초기화
        MDC.clear();

        // 응답 본문 복사
        cachingResponseWrapper.copyBodyToResponse();
    }
}
