package com.ljm.boot.jwttoken.config;


import com.ljm.boot.jwttoken.annotation.IgnoreWebSecurity;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/20 17:44
 **/
@Configuration
public class InitIgnoreWebSecurityConfig implements ApplicationContextAware {

    @Autowired
    private IgnoreSecurityPropetties ignoreSecurityPropetties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RestController.class);
        beanMap.forEach((k, v) -> {

            //Controllers配置的访问路径
            String baseUrl = "";
            Class<?> controllerClass = v.getClass();
            RequestMapping annotation = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);

            //如果RequestMapping注解存在,使用RequestMapping里配置的路径名称
            if (annotation != null) {
                baseUrl = annotation.value().length > 0 ? annotation.value()[0] : "";
            }

            //判断访问路径前缀是否包含/
            if (!baseUrl.startsWith("/")) {
                baseUrl = "/" + baseUrl;
            }

            //获取所有声明的方法
            Method[] allMethods = controllerClass.getMethods();
            IgnoreWebSecurity ignoreWebSecurity;
            PostMapping postMapping;
            GetMapping getMapping;
            String methodType;

            for (Method method : allMethods) {
                //判断方法是否使用忽略权限认证注解
                ignoreWebSecurity = AnnotatedElementUtils.findMergedAnnotation(method, IgnoreWebSecurity.class);
                if (ignoreWebSecurity != null) {
                    String url = "";
                    methodType = "";
                    //当注解没配置接口名称时候使用接口名称(Controller访问路径+接口访问路径)
                    //目前只适配了PostMapping和GetMapping注解,其它类型请自行扩展
                    postMapping = AnnotatedElementUtils.findMergedAnnotation(method, PostMapping.class);
                    if (postMapping != null) {
                        url = postMapping.value().length > 0 ? postMapping.value()[0] : "";
                        methodType = "post";
                    } else {
                        getMapping = AnnotatedElementUtils.findMergedAnnotation(method, GetMapping.class);
                        if (getMapping != null) {
                            url = getMapping.value().length > 0 ? getMapping.value()[0] : "";
                            methodType = "get";
                        }
                    }
                    //注解如果配置了接口路径则使用注解内的
                    if (StringUtils.hasText(ignoreWebSecurity.value())) {
                        url = ignoreWebSecurity.value();
                    }
                    if (url.trim().length() > 0) {
                        url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    } else {
                        url = baseUrl;
                    }
                    if ("post".equals(methodType)) {
                        ignoreSecurityPropetties.getPost().add(url);
                    } else if ("get".equals(methodType)) {
                        ignoreSecurityPropetties.getGet().add(url);
                    }
                }
            }
        });
        System.out.println("需要注解忽略的get请求类型接口路径如下" );
        for(String get: ignoreSecurityPropetties.getGet()){
            System.out.println(get);
        }
        System.out.println("需要注解忽略的post请求类型接口路径如下");
        for(String post: ignoreSecurityPropetties.getPost()){
            System.out.println(post);
        }
    }

}
