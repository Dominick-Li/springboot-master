package com.ljm.boot.redissession.config;

import com.ljm.boot.redissession.interceptor.RedisSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 20)//session过期时间(秒)
@Configuration
public class RedisSessionConfig implements WebMvcConfigurer {
    @Autowired
    RedisSessionInterceptor redisSessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        //所有已api开头的访问都要进入RedisSessionInterceptor拦截器进行登录验证;
        registry.addInterceptor(redisSessionInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/login/**");
    }
}
