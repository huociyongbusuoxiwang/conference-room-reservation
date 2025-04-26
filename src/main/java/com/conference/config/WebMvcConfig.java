package com.conference.config;

import com.conference.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    @Resource
    private LoginInterceptor loginInterceptor;  // 先将拦截器注入到ioc容器中，否则StringRedisTemplate对象会一直为空

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("**/login", "**/register"); // 登录和注册不拦截
    }
}