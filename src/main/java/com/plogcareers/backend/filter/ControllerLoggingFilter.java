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
        String requestID = request.getHeader("requestID");
        String caller = "";

        // caller 가져오기
        if (handlerExecutionChain != null) {
            Object handler = handlerExecutionChain.getHandler();
            if (handler instanceof HandlerMethod handlerMethod) {
                caller = String.format("%s.%s", handlerMethod.getBeanType().getSimpleName(), handlerMethod.getMethod().getName());
            }
        }

        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        String requestBody = new String(cachingRequestWrapper.getContentAsByteArray());
        String responseBody = new String(cachingResponseWrapper.getContentAsByteArray());

        // 로깅 필드 지정
        MDC.put("caller", caller);
        MDC.put("requestID", requestID);
        MDC.put("requestURL", cachingRequestWrapper.getRequestURL().toString());
        MDC.put("requestBody", StringUtil.isNullOrEmpty(requestBody) ? "null" : requestBody);
        MDC.put("responseBody", StringUtil.isNullOrEmpty(responseBody) ? "null" : responseBody);
        MDC.put("status", String.valueOf(cachingResponseWrapper.getStatus()));

        logger.info("");

        MDC.clear();

        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);

        cachingResponseWrapper.copyBodyToResponse();
    }
}
