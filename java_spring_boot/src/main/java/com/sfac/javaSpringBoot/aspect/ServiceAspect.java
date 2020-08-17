package com.sfac.javaSpringBoot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceAspect.class);

    //切入到的路径
    @Pointcut("@annotation(com.sfac.javaSpringBoot.aspect.ServiceAnnotation)")
    @Order(2)
    public void servicePointCut(){

    }

    //前置通知
    @Before(value = "com.sfac.javaSpringBoot.aspect.ServiceAspect.servicePointCut()")
    public void beforeService(JoinPoint joinPoint){
        LOGGER.debug("======= this is before controller =======");
    }

    //环绕通知
    @Around("com.sfac.javaSpringBoot.aspect.ServiceAspect.servicePointCut()")
    public Object aroundService (ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        LOGGER.debug("======= this is around controller =======");
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @After("com.sfac.javaSpringBoot.aspect.ServiceAspect.servicePointCut()")
    public void afterService(){
        LOGGER.debug("======= This is after controller =======");
    }
}
