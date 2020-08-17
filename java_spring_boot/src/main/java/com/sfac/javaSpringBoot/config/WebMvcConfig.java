package com.sfac.javaSpringBoot.config;

import com.sfac.javaSpringBoot.filter.RequestParamaFilter;
import com.sfac.javaSpringBoot.interceptor.RequestViewInterceptor;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class WebMvcConfig implements WebMvcConfigurer {
//为了注册拦截器，所以实现了一个接口WebMvcConfigurer，并重写一个addInterceptors方法

    @Value("${server.http.port}")
    private int httpPort;

    @Autowired
    private RequestViewInterceptor requestViewInterceptor;

    //创建连接器
    @Bean
    public Connector connector(){
        Connector connector = new Connector();
        //https设置后，全局配置中的端口号被占用了，所以现在给http设置段楼号为80
        connector.setPort(httpPort);
        //设置编码格式
        connector.setScheme("http");
        return connector;
    }

    //将连接器设置到容器里面去（tomcat）
    @Bean
    public ServletWebServerFactory webServerFactory(){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(connector());
        return tomcat;
    }

    //注册过滤器
    @Bean
    public FilterRegistrationBean<RequestParamaFilter> register(){
        FilterRegistrationBean<RequestParamaFilter> register
                = new FilterRegistrationBean<RequestParamaFilter>();
        register.setFilter(new RequestParamaFilter());
        return register;
    }

    //注册拦截器（没用bean用于返回一个实体bean,是因为是void，并且只是实现类并重写了一个方法）
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器并将所有的请求纳入到拦截器中
        registry.addInterceptor(requestViewInterceptor).addPathPatterns("/**");
    }
}
