package com.ljm.boot.jwttoken.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/22 12:13
 **/
@Configuration
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreSecurityPropetties {

    /**
     * 需要忽略的接口路径 不限制请求类型
     */
    private List<String> all;

    /**
     * 需要忽略的Get请求类型接口路径
     */
    private List<String> get;

    /**
     * 需要忽略的Post请求类型接口路径
     */
    private List<String> post;

    public List<String> getGet() {
        return get;
    }

    public void setGet(List<String> get) {
        this.get = get;
    }

    public List<String> getPost() {
        return post;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }

    public List<String> getAll() {
        return all;
    }

    public void setAll(List<String> all) {
        this.all = all;
    }
}
