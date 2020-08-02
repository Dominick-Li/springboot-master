package com.ljm.boot.jwttoken.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Dominick Li
 * @CreateTime 2020/4/16 22:09
 * @description token认证失败返回401异常
 **/

public class MyAuthExcetion {

    @Component
    public static class MyAuthenticationEntry implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            //token认证失败
            HashMap map = new HashMap();
            map.put("code", 402);
            map.put("msg", "toekn is invalid");
            response.getWriter().println(map.toString());
        }
    }

    @Component
    public static class MyAccessDenied implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
            //角色权限认证失败
            HashMap map = new HashMap();
            map.put("code", 401);
            map.put("msg", "accessDenied");
            response.getWriter().println(map.toString());
        }
    }

}
