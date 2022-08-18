package com.example.take_out.filter;

import com.alibaba.fastjson.JSON;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 检查用户是否已经完成登陆
 */

@Slf4j
@Component
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")  //设置拦截器，设置拦截的网页区域
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符，因为下面的序列使用了通配符
    // AntPathMatcher匹配规则  ? 匹配一个字符    * 匹配0个或多个字符   ** 匹配0个或多个目录/字符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //  A. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截的请求：{}", requestURI);

        // 定义不拦截的序列，只拦截页面数据请求，不拦截页面格式
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/user/login",
                "/user/logout",
                "/user/sendMsg",
                "/front/**",
                "/common/**"
        };
        //  B. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls, requestURI);
        //  C. 如果不需要，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //  D. 判断登录状态，如果已登录，则直接放行
        Long empId = (Long) request.getSession().getAttribute("employee");
        if (empId != null) {
            log.info("用户已登录，用户id为：{}", empId);
            BaseContext.setId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            log.info("用户已登录，用户id为：{}", userId);
            BaseContext.setId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        //  E. 如果未登录, 则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check(String[] urls, String requestURI) {
        /*
          路径匹配
          @Description: 路径匹配，检查本次请求是否需要放行
         * @author LiBiGo
         * @date 2022/8/12 16:50
         */
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI); // 使用通配符对象，配对资源
            if (match) {
                return true;
            }
        }
        return false;
    }
}