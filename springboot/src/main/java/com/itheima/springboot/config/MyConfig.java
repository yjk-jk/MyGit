package com.itheima.springboot.config;//package com.itheima.springboot.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    PageInterceptor pageInterceptor(){
        return new PageInterceptor();
    }

}
