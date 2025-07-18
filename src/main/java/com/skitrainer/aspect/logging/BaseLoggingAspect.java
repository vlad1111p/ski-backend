package com.skitrainer.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
public abstract class BaseLoggingAspect {

    protected abstract String getLayerName();

    protected abstract String getPointcut();

    @Before("getPointcut()")
    public void logBefore(final JoinPoint joinPoint) {
        log.info("[{}] Entering method: {} with arguments: {}",
                getLayerName(),
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "getPointcut()", returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint, final Object result) {
        boolean isVoid = ((MethodSignature) joinPoint.getSignature()).getReturnType().equals(Void.TYPE);
        if (isVoid) {
            log.info("[{}] [{}] Exiting method: {}",
                    Thread.currentThread().getName(),
                    getLayerName(),
                    joinPoint.getSignature().toShortString());
        } else {
            log.info("[{}] [{}] Exiting method: {} with result: {}",
                    Thread.currentThread().getName(),
                    getLayerName(),
                    joinPoint.getSignature().toShortString(),
                    result);
        }
    }

    @AfterThrowing(pointcut = "getPointcut()", throwing = "exception")
    public void logAfterThrowing(final JoinPoint joinPoint, final Throwable exception) {
        log.error("[{}] [{}] Exception in method: {} with message: {}",
                Thread.currentThread().getName(),
                getLayerName(),
                joinPoint.getSignature().toShortString(),
                exception.getMessage(), exception);
    }
}
