package com.plogcareers.backend.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plogcareers.backend.common.component.ArgumentLogFormatter;
import com.plogcareers.backend.common.component.ErrorLogFormatter;
import com.plogcareers.backend.common.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Aspect
@Slf4j
public class ServiceRepositoryLoggingAspect {
    private final static String REQUEST_ID_HEADER = "requestID";

    private final ErrorLogFormatter errorLogFormatter;
    private final ArgumentLogFormatter argumentLogFormatter;
    private final ObjectMapper mapper;

    @Pointcut("within(com.plogcareers.backend..service..*) || (execution(* com.plogcareers.backend..repository..*.*(..)) && within(org.springframework.data.repository.Repository+))")
    public void serviceRepository() {
    }

    private String getRequestID() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader(REQUEST_ID_HEADER);
    }

    private String getCaller(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 메서드가 JpaRepository에서 상속된 경우 실제 리포지토리 인터페이스 이름을 사용
        if (method.getDeclaringClass().isAssignableFrom(JpaRepository.class)) {
            // 대상 인터페이스를 상속받는 최하위 인터페이스를 가져옵니다.
            Class<?> targetClass = joinPoint.getTarget().getClass().getInterfaces()[0];

            return String.format("%s.%s", targetClass.getName(), method.getName());
        }

        return String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
    }

    private String[] getParameterNames(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    }

    private Object[] getArgValues(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    @Before("serviceRepository()")
    public void beforeServiceAndRepositoryRequestLogging(JoinPoint joinPoint) {
        MDC.put("requestID", getRequestID());
        MDC.put("caller", getCaller(joinPoint));
        MDC.put("request", argumentLogFormatter.json(getParameterNames(joinPoint), getArgValues(joinPoint)));

        // FIXME: 로그 필드를 동적으로 설정하기
        MDC.put("response", "null");
        MDC.put("exception", "null");

        log.info("start");

        MDC.clear();
    }

    @AfterReturning(pointcut = "serviceRepository()", returning = "returnValue")
    public void afterServiceAndRepositoryResponseLogging(JoinPoint joinPoint, Object returnValue) throws JsonProcessingException {
        MDC.put("requestID", getRequestID());
        MDC.put("caller", getCaller(joinPoint));
        MDC.put("response", mapper.writeValueAsString(returnValue));

        // FIXME: 로그 필드를 동적으로 설정하기
        MDC.put("request", "null");
        MDC.put("exception", "null");

        log.info("success");

        MDC.clear();
    }

    @AfterThrowing(pointcut = "serviceRepository()", throwing = "exception")
    public void afterServiceAndRepositoryThrowingLogging(JoinPoint joinPoint, Throwable exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestID = request.getHeader("requestID");
        String className = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        MDC.put("requestID", requestID);
        MDC.put("caller", String.format("%s.%s", className, methodName));
        MDC.put("exception", errorLogFormatter.json(exception));

        // FIXME: 로그 필드를 동적으로 설정하기
        MDC.put("request", argumentLogFormatter.json(getParameterNames(joinPoint), getArgValues(joinPoint)));
        MDC.put("response", "null");

        // 에러 타입에 따른 로그 레벨 지정
        if (exception instanceof UserException) {
            log.warn("client error");
        } else {
            log.error("internal server error");
        }

        MDC.clear();
    }
}
