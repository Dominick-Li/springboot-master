package com.ljm.boot.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



/**
 * @Description
 * @Author lijunming
 * @Date 2019/12/17 15:56
 **/
@Component
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailConfig {

    /**
     * 邮箱服务的地址
     * qq邮箱=smtp.qq.com   腾讯企业邮箱=smtp.exmail.qq.com
     */
    private String  host;

    /**
     * 邮箱服务使用的端口
     * qq邮箱=25  腾讯企业邮箱=465
     */
    private Integer  port;

    /**
     * 设置ssl访问
     * qq邮箱=false  腾讯企业邮箱=true
     */
    private boolean ssl;

    /**
     * 发件人
     */
    private String  username;

    /**
     * 客户端授权码
     */
    private String  password;

    /**
     * 发件人的昵称
     */
    private String  formName;

    /**
     * 收件人的邮箱账号
     */
    @Value("#{'${mail.to}'.split(',')}")
    private String[] to;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
