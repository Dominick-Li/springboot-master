package com.ljm.boot.configswitch.config;

import com.ljm.boot.configswitch.filter.LogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/16 22:57
 * @description 根据条件决定要不要加载此类
 **/
//@ConditionalOnWebApplication  //方式一

/**
 * 通过其两个属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。
 * 如果该值为空，则返回false;
 * 如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。
 * 如果返回值为false，则该configuration不生效；为true则生效。
 */
@ConditionalOnProperty(name = "mylog.enable", havingValue = "true")
@Configuration
public class LogFilterWebConfig {

    @Bean
    public LogFilter buildFilter() {
        return new LogFilter();
    }
}
