package com.ljm.boot.i18n.config;


import com.ljm.boot.i18n.filter.HttpInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry interceptor) {
        //需要配置拦截器并指定拦截的接口路径
        interceptor.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }
}