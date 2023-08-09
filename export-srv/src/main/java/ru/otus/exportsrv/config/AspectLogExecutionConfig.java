package ru.otus.exportsrv.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class AspectLogExecutionConfig {

    @Pointcut("@target(ru.otus.exportsrv.config.AspectLogExecuteTime) && within(ru.otus.exportsrv..*)")
    public void servicePointcut() {
    }

    @Around("servicePointcut()")
    public Object serviceResponseTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return monitorResponseTime(proceedingJoinPoint);
    }

    private Object monitorResponseTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        String serviceName = getClassName(proceedingJoinPoint.getThis().getClass().getName());
        String methodName = proceedingJoinPoint.getSignature().getName();
        long duration = (end - start);
        log.debug("class: {} method: {} duration millis: {}", serviceName, methodName, duration);
        return obj;
    }

    private String getClassName(String fullName) {
        String serviceClass = fullName;
        int first = serviceClass.lastIndexOf('.')+1;
        int second = serviceClass.indexOf('$');
        if (first > 0 && second > first) {
            serviceClass = serviceClass.substring(first, second);
        }
        return serviceClass;
    }
}