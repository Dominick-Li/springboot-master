package com.ljm.boot.simplifycode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
    private static final String ERROR_MESSAGE = "系统内部错误,请联系管理员！";

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String ExceptionHndler(Exception e) {
        if (e instanceof FileNotFoundException) {
            //可以自定义根据不同的异常类型,做一些代码处理
        }
        log.error("global error:{}", e);
        return ERROR_MESSAGE;
    }
}



