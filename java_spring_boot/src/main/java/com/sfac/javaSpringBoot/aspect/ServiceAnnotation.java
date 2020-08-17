package com.sfac.javaSpringBoot.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD) //目标
@Documented                 //文档型
@Retention(RetentionPolicy.RUNTIME) //作用范围：RUNTIME

//自定义的属性配置
public @interface ServiceAnnotation {
    String value() default "aaaa";
}
