package com.skitrainer.aspect.logging.impl;


import com.skitrainer.aspect.logging.BaseLoggingAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect extends BaseLoggingAspect {

    @Override
    protected String getLayerName() {
        return "Controller";
    }

    @Pointcut("execution(* com.skitrainer.controller..*(..))")
    @Override
    protected String getPointcut() {
        return "execution(* com.skitrainer.controller..*(..))";
    }
}
