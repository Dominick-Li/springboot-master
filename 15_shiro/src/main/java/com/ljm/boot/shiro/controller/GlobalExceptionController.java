package com.ljm.boot.shiro.controller;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ERROR_MESSAGE = "系统内部错误,请联系管理员！";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView ExceptionHndler(Exception e) {
        ModelAndView mv = new ModelAndView("error");
        String msg = ERROR_MESSAGE;
        if (e instanceof UnauthorizedException) {
            msg = e.getMessage();
        }
        mv.addObject("error", msg);
        logger.error("global error:{}", e);
        return mv;
    }
}



