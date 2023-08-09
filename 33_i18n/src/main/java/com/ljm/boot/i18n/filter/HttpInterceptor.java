package com.ljm.boot.i18n.filter;

import com.ljm.boot.i18n.util.ThreadLocalManagerUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Dominick Li
 * @Description 需要注册到 webmvc中
 * @CreateTime 2022/12/8 17:59
 **/
public class HttpInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //会话结束移除线程缓存
        ThreadLocalManagerUtil.remove();
        return;
    }
}
