package com.example.take_out.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登陆
 */
@Slf4j
// @Component
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截请求：{}", request.getRequestURI());
        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        // 定义不需处理的uri
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout"
        };
        // 2、判断本次请求是否需要处理

        // 3、如果不需要处理，则直接放行
        // 4、判断登录状态，如果已登录，则直接放行
        // 5、如果未登录则返回未登录结果

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
