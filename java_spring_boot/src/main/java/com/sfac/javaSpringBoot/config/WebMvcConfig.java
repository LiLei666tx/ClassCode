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
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//用@Configuration标识的类就是配置类，而不是什么WebMvcConfig类（而应该说配置类）
@Configuration
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class WebMvcConfig implements WebMvcConfigurer {
//为了注册拦截器，所以实现了一个接口WebMvcConfigurer，并重写一个addInterceptors方法

    @Value("${server.http.port}")
    private int httpPort;

    @Autowired
    private RequestViewInterceptor requestViewInterceptor;

    @Autowired
    private ResourceConfigBean resourceConfigBean;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String osName = System.getProperty("os.name");
        //判断是哪一种系统，看是Windows害死Linux，其他的暂未说明
        if(osName.startsWith("win")) {
            registry.addResourceHandler(resourceConfigBean.getRelativePathPattern())
                    .addResourceLocations(ResourceUtils.FILE_URL_PREFIX +
                            resourceConfigBean.getLocationPathForWindows());
        }else {
            registry.addResourceHandler(resourceConfigBean.getRelativePathPattern())
                    .addResourceLocations(ResourceUtils.FILE_URL_PREFIX +
                            resourceConfigBean.getLocationPathForLinux());
        }
    }
}
