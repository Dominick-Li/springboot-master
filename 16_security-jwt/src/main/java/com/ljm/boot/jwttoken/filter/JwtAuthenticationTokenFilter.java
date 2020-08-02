package com.ljm.boot.jwttoken.filter;


import com.ljm.boot.jwttoken.util.JwtTokenUtils;
import com.ljm.boot.jwttoken.security.JwtUserDetail;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dominick Li
 * @CreateTime 2020/4/16 21:45
 * @description 权限认证
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(jwtTokenUtils.getTokenHead());
        //判断当前请求中包含令牌
        if (!StringUtils.isEmpty(token)) {
            //重token中获取用户的角色权限信息
            Claims claims = jwtTokenUtils.getTokenClaim(token);
            if (claims != null) {
                //如果token未失效 并且 当前上下文权限凭证为null
                if (!jwtTokenUtils.isTokenExpired(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    /**
                     * 这里省略了查询数据库token存不存在,有没有失效这个步骤
                     * 如果是做单点登录,后登录的人登录时候把上一个人的token状态改为失效,这样就能保证同一时间一个帐号只能有一个人能使用
                     */
                    JwtUserDetail userDetails = new JwtUserDetail(claims);
                    //把用户权限信息放到上下文中
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
