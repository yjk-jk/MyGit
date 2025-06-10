package com.itheima.springboot.config;

import com.itheima.springboot.intercept.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册一个拦截器，拦截所有请求，并在请求前后打印日志
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns()
                .excludePathPatterns("/**");

    }
}
