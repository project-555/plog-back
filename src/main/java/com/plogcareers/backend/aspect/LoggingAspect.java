package com.plogcareers.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.plogcareers.backend.*.controller.*)")
    public void controller() {
    }

    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("###Start request {}", joinPoint.getSignature().toShortString());
        joinPoint.getArgs();

        for (Object arg : joinPoint.getArgs()) {
            log.info("\t{}", arg);
        }
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("###End request {}", joinPoint.getSignature().toShortString());
        if (returnValue == null) return;
        log.info("\t{}", returnValue);
    }

}
