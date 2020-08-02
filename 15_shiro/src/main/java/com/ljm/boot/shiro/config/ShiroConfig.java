package com.ljm.boot.shiro.config;

import com.ljm.boot.shiro.shiro.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    /**
     * 配置自定义Realm
     */
    @Bean
    public CustomRealm userRealm() {
        CustomRealm userRealm = new CustomRealm();
        //配置使用哈希密码匹配
        userRealm.setCredentialsMatcher(credentialsMatcher());
        return userRealm;
    }

    /**
     * 设置对应的过滤条件和跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //没有登录的用户请求需要登录的页面时自动跳转到登录页面。
        shiroFilterFactoryBean.setLoginUrl("/login");
        //filterChainDefinitionMap 配置过滤规则，从上到下的顺序匹配。
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //authc是需要认证后访问的接口,anon是放行的接口
        filterChainDefinitionMap.put("/user/**", "authc");
        filterChainDefinitionMap.put("/admin/**", "authc");
        //除了user和admin路径开头的接口,其它资源都开放
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        //没有权限默认跳转的页面，登录的用户访问了没有被授权的资源自动跳转到的页面。
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        return shiroFilterFactoryBean;
    }


    /**
     * 设置用于匹配密码的CredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        // 散列算法，这里使用更安全的sha256算法
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        // 数据库存储的密码字段使用HEX还是BASE64方式加密
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        // 散列迭代次数
        credentialsMatcher.setHashIterations(1024);
        return credentialsMatcher;
    }

    /**
     * 配置security并设置userReaml，避免xxxx required a bean named 'authorizer' that could not be found.的报错
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        return securityManager;
    }


    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator app = new DefaultAdvisorAutoProxyCreator();
        app.setProxyTargetClass(true);
        return app;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

}
