package com.sfac.javaSpringBoot.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

//创建过滤器(@Order,当有多个过滤器时，用注解@Order(数字)来确定先后，数字越小的越靠前)
@WebFilter(filterName = "requestParamaFilter", urlPatterns = "/**") //对所有的请求都生效
public class RequestParamaFilter implements Filter {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestParamaFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("--------init request param filter");
    }

    //实现filter的业务逻辑
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("--------dofilter request param filter---------");
        //强转ServletResponse ----> HttpServletRequest
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        Map<String, String[]> paramsMap = httpServletRequest.getParameterMap();
//        paramsMap.put("paramkey",new String[]{"****"});

        //因为获取到的是locked的，所以用包装类wrapper来包装 httpServletRequest
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpServletRequest){
            @Override
            public String getParameter(String name) {
                String value = httpServletRequest.getParameter(name);
                if(StringUtils.isNotBlank(value)){
                    return value.replaceAll("shit","****");
                }
                return super.getParameter(name);
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = httpServletRequest.getParameterValues(name);
                if(values != null && values.length > 0){
                    for (int i = 0;i < values.length;i++) {
                        values[i] = values[i].replaceAll("shit","****");
                    }
                    return values;
                }
                return super.getParameterValues(name);
            }
        };
        //因为request被包装类wrapper包装了，所以用的是wrapper来代替request
        //当写完业务之后，一定要用chain.dofilter把request和response交出去，否则上面的操作白写了
        chain.doFilter(wrapper, response);
    }

    @Override
    public void destroy() {
        LOGGER.debug("--------destroy request param filter");
    }
}
