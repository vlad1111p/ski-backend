package com.skitrainer.aspect.logging.impl;


import com.skitrainer.aspect.logging.BaseLoggingAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class ControllerLoggingAspect extends BaseLoggingAspect {

    public static final String EXECUTION_COM_SKITRAINER_CONTROLLER = "execution(* com.skitrainer.controller..*(..))";

    @Override
    protected String getLayerName() {
        return "Controller";
    }

    @Pointcut(EXECUTION_COM_SKITRAINER_CONTROLLER)
    @Override
    protected String getPointcut() {
        return EXECUTION_COM_SKITRAINER_CONTROLLER;
    }
}
