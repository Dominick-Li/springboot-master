package com.ljm.boot.validation.controller;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/5/10 12:35
 **/
@ControllerAdvice
public class ControllerException {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidException(MethodArgumentNotValidException e) {
        //将错误信息返回给前台
        String field, msg;
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            // 获取错误验证字段名
            field = fieldError.getField();
            msg = fieldError.getDefaultMessage();
            sb.append("参数名[").append(field).append("]").append(msg).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
