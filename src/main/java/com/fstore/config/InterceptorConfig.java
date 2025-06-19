package com.fstore.config;

import com.fstore.interceptor.DefaultUserAuthInterceptor;
import com.fstore.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {
    @Bean
    public UserAuthInterceptor localUserAuthInterceptor() {
        return new DefaultUserAuthInterceptor();
    }
}
