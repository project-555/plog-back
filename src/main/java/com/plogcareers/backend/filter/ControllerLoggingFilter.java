package com.plogcareers.backend.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class ControllerLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestID = UUID.randomUUID().toString();

        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        byte[] requestBodyBytes = cachingRequestWrapper.getContentAsByteArray();
        String requestBody = new String(requestBodyBytes);
        requestBody = requestBody.isEmpty() ? "null" : requestBody;

        String requestURL = cachingRequestWrapper.getRequestURL().toString();

        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);
        byte[] responseBodyBytes = cachingResponseWrapper.getContentAsByteArray();
        String responseBody = new String(responseBodyBytes);
        responseBody = responseBody.isEmpty() ? "null" : responseBody;

        int intStatus = cachingResponseWrapper.getStatus();
        String status = String.valueOf(intStatus);

        MDC.put("requestID", requestID);
        MDC.put("requestURL", requestURL);
        MDC.put("requestBody", requestBody);
        MDC.put("responseBody", responseBody);
        MDC.put("status", status);
        logger.info("null");
        MDC.clear();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
    }
}
