package com.sfac.javaSpringBoot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component //注册位系统的一个组件
public class ControllerAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    /**
     * 关联在方法上的切点
     * 第一个*代表返回类型不限
     * 第二个*代表module下所有子包
     * 第三个*代表所有类
     * 第四个*代表所有方法
     * (..) 代表参数不限
     * Order 代表优先级，数字越小优先级越高
     */

    /**
     * 分为三个步骤，1、添加切面程序，@Aspect
     *              2、设置切片，绑定切入的位置，@Pointcut（有两种方式，一种是包全路径，二是自定义注释，需要new 一个 Annotation）
     *              3、进行通知的书写 void controllerPointCut()
     */
    @Pointcut("execution(public * com.sfac.javaSpringBoot.modules.*.controller.*.*(..))")
    @Order(1)
    public void controllerPointCut(){ }

    //（因为上面的包全路径是不精确的，因此下面的@Before、@Around、@After 都是增强程序，提供精确定位）
    //前置通知
    @Before(value = "com.sfac.javaSpringBoot.aspect.ControllerAspect.controllerPointCut()")
    public void beforeController(JoinPoint joinPoint){
        LOGGER.debug("======= this is before controller =======");
        ServletRequestAttributes attributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LOGGER.debug("请求来源：" + request.getRemoteAddr());
        LOGGER.debug("请求URL：" + request.getRequestURL().toString());
        LOGGER.debug("请求方式：" + request.getMethod());
        LOGGER.debug("响应方法：" +
                joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        LOGGER.debug("请求参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    //环绕通知（注意返回的是Object）
    @Around("com.sfac.javaSpringBoot.aspect.ControllerAspect.controllerPointCut()")
    public Object aroundController (ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        LOGGER.debug("======= this is around controller =======");
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @After("com.sfac.javaSpringBoot.aspect.ControllerAspect.controllerPointCut()")
    public void afterController(){
        LOGGER.debug("======= This is after controller =======");
    }

}
