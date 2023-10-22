package com.plogcareers.backend.filter;


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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ControllerLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingFilter.class);
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        // requestID 가져오기
        String requestID = request.getHeader("requestID");

        // caller 가져오기
        String caller = "";
        if (handlerExecutionChain != null) {
            Object handler = handlerExecutionChain.getHandler();
            if (handler instanceof HandlerMethod handlerMethod) {
                caller = String.format("%s.%s", handlerMethod.getBeanType().getSimpleName(), handlerMethod.getMethod().getName());
            }
        }

        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);

        String requestBody = new String(cachingRequestWrapper.getContentAsByteArray());
        String responseBody = new String(cachingResponseWrapper.getContentAsByteArray());

        // 로깅 필드 지정
        MDC.put("caller", caller);
        MDC.put("requestID", requestID);
        MDC.put("requestURL", cachingRequestWrapper.getRequestURL().toString());
        MDC.put("requestBody", StringUtil.isNullOrEmpty(requestBody) ? "null" : requestBody);
        MDC.put("responseBody", StringUtil.isNullOrEmpty(responseBody) ? "null" : responseBody);
        MDC.put("status", String.valueOf(cachingResponseWrapper.getStatus()));

        // 응답에 따른 에러 로그 레벨 지정
        switch (cachingResponseWrapper.getStatus() / 100) {
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
                logger.error("intenal server error");
                break;
            default:
                break;
        }

        // 로그 필드 초기화
        MDC.clear();

        // 응답 본문 복사
        cachingResponseWrapper.copyBodyToResponse();
    }
}
