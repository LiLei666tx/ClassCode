package com.sfac.javaSpringBoot.modules.common.controller;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice           //注解类(这里统一处理异常)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerController {

    @ExceptionHandler(value = AuthorizationException.class)     //注解（想处理啥异常就写哪个异常）
    @ResponseBody
    public Result<String> handle403(){
        return new Result<>(Result.ResultStatus.FAILD.status,
                "","/common/page_403");
    }
}
