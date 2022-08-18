package com.example.take_out.config;

import com.example.take_out.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
public class LoginCheckConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new LoginCheckInterceptor());
        // 配置所有路径都进行拦截
        interceptor.addPathPatterns("/**");
        // 配置不拦截路径
        interceptor.excludePathPatterns(
                "/employee/login",
                "/employee/logout",
                "/backend/api/**",
                "/backend/images/**",
                "/backend/js/**",
                "/backend/page/**",
                "/backend/plugins/**",
                "/backend/styles/**",
                "/user/login",
                "/user/logout",
                "/front/api/**",
                "/front/images/**",
                "/front/js/**",
                "/front/page/**",
                "/front/plugins/**",
                "/front/styles/**"
        );
    }
}
