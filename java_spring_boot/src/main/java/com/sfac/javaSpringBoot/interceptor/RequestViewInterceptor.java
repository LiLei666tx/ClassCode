package com.sfac.javaSpringBoot.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component是注册为系统的bean
@Component
public class RequestViewInterceptor implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestViewInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.debug("==== preHandle interceptor ====");
        return HandlerInterceptor.super.preHandle(request,response,handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        LOGGER.debug("==== postHandle interceptor ====");

        if (modelAndView == null || modelAndView.getViewName().startsWith("redirect")) {
            return;
        }

        //拦截器获取到页面文件路径
        String path = request.getServletPath();
        String template = (String)modelAndView.getModelMap().get("template");
        //去掉path路径前面的“/”
        if(StringUtils.isBlank(template)){
            if(path.startsWith("/")){
                path = path.substring(1);
            }
            //将处理后的path add到“template”中，并转为小写
            modelAndView.getModelMap().addAttribute(
                    "template", path.toLowerCase());
        }
         HandlerInterceptor.super.preHandle(request,response,handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOGGER.debug("==== afterCompletion interceptor ====");
         HandlerInterceptor.super.preHandle(request,response,handler);
    }
}
