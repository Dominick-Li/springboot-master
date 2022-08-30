package com.ljm.boot.lowcode.security;

import com.ljm.boot.lowcode.config.JwtTokenConfig;
import com.ljm.boot.lowcode.repository.system.SysTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 * @description toekn认证
 **/
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private SysTokenRepository sysTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(jwtTokenConfig.getTokenHead());
        String username;
        //当前请求中包含令牌
        if (!StringUtils.isEmpty(token)) {
            Claims claims = jwtTokenConfig.getTokenClaim(token);
            if (claims != null) {
                username = claims.getSubject();
                //***
                if (!jwtTokenConfig.isTokenExpired(token) && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {
                    //查询数据库,查询数据库token是否还存在,如果不存在则被别人挤下线了
                    if (sysTokenRepository.findTopByAccessToken(token) != null) {
                        org.springframework.security.core.userdetails.UserDetails userDetails = new MyUserDetail(claims);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        logger.debug("authorication user: " + username + ", setting security context");
                        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        //用户在别的地方已登录
                        logger.debug("authorication user: " + username + ",  other client login");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
