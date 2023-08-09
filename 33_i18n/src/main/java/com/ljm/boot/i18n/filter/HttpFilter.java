package com.ljm.boot.i18n.filter;

import com.ljm.boot.i18n.constant.SystemConstants;
import com.ljm.boot.i18n.util.ThreadLocalManagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/8 17:57
 **/
@Component
public class HttpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ThreadLocalManagerUtil.HeaderInfo headerInfo  = new ThreadLocalManagerUtil.HeaderInfo();
        headerInfo.setLanguage(request.getHeader(SystemConstants.LANGUAGE));
        headerInfo.setToken(request.getHeader(SystemConstants.TOKEN_NAME));
        //在ThreadLocal中添加token和当前国际化信息
        ThreadLocalManagerUtil.add(headerInfo);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

}
