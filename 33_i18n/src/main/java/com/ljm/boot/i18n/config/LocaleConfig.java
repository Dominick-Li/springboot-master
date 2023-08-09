package com.ljm.boot.i18n.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author Dominick Li
 * @Description 国际化配置
 * @CreateTime 2022/12/8 16:19
 **/
@Configuration
public class LocaleConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        Locale.setDefault(Locale.CHINA);
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        //设置国际化文件存储路径   resources目录下
        source.setBasenames("i18n/messages");
        //设置根据key如果没有获取到对应的文本信息,则返回key作为信息
        source.setUseCodeAsDefaultMessage(true);
        //设置字符编码
        source.setDefaultEncoding("UTF-8");
        return source;
    }

}
