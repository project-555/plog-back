package com.plogcareers.backend.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plogcareers.backend.common.component.ArgumentLogFormatter;
import com.plogcareers.backend.common.component.ErrorLogFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
@Aspect
@Slf4j
public class ServiceRepositoryLoggingAspect {
    private final ErrorLogFormatter errorLogFormatter;
    private final ArgumentLogFormatter argumentLogFormatter;
    private final ObjectMapper mapper;


    @Pointcut("within(com.plogcareers.backend.*.service.*) || within(com.plogcareers.backend.*.repository.*)")
    public void serviceRepository() {
    }

    @Before("serviceRepository()")
    public void beforeServiceAndRepositoryRequestLogging(JoinPoint joinPoint) {
        String requestID = UUID.randomUUID().toString();
        String className = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        MDC.put("requestID", requestID);
        MDC.put("caller", String.format("%s.%s", className, methodName));
        MDC.put("request", argumentLogFormatter.json(parameterNames, argValues));
        MDC.put("response", "null");
        MDC.put("exception", "null");
        log.info("start");
        MDC.clear();
    }

    @AfterReturning(pointcut = "serviceRepository()", returning = "returnValue")
    public void afterServiceAndRepositoryResponseLogging(JoinPoint joinPoint, Object returnValue) throws JsonProcessingException {
        String requestID = UUID.randomUUID().toString();
        String className = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        MDC.put("requestID", requestID);
        MDC.put("caller", String.format("%s.%s", className, methodName));
        MDC.put("request", "null");
        MDC.put("response", mapper.writeValueAsString(returnValue));
        MDC.put("exception", "null");
        log.info("end");
        MDC.clear();
    }

    @AfterThrowing(pointcut = "serviceRepository()", throwing = "exception")
    public void afterServiceAndRepositoryThrowingLogging(JoinPoint joinPoint, Throwable exception) {
        String requestID = UUID.randomUUID().toString();
        String className = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        MDC.put("requestID", requestID);
        MDC.put("caller", String.format("%s.%s", className, methodName));
        MDC.put("request", "null");
        MDC.put("response", "null");
        MDC.put("exception", errorLogFormatter.json(exception));
        log.error("error");
        MDC.clear();
    }
}
