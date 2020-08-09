package com.ljm.boot.swagger.model;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@ApiModel("Http结果响应")
public class HttpResult<T> implements Serializable {

    private static String successMessage = "操作成功";

    private static String errorMessage = "操作失败";

    @ApiModelProperty("响应码")
    private int code;

    @ApiModelProperty("响应数据")
    private T data;

    @ApiModelProperty("响应消息")
    private String msg;

    public HttpResult(){ }

    public HttpResult(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public HttpResult(int code, String msg, T data) {
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static HttpResult successResult() {
        return new HttpResult(HttpStatus.OK.value(), successMessage);
    }

    public static HttpResult successResult(String msg) {
        return new HttpResult(HttpStatus.OK.value(), defaultSuccessMsg(msg));
    }

    public static HttpResult successResult(Object obj) {
        return new HttpResult(HttpStatus.OK.value(), successMessage, obj);
    }

    public static HttpResult successResult(String msg, Object obj) {
        return new HttpResult(HttpStatus.OK.value(), defaultSuccessMsg(msg), obj);
    }

    public static HttpResult failureResult() {
        return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
    }

    public static HttpResult failureResult(String msg) {
        return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), defautlErrorMsg(msg));
    }

    public static HttpResult failureResult(Integer code, String msg) {
        return new HttpResult(code, defautlErrorMsg(msg));
    }

    public static HttpResult failureResult(String msg, Object obj) {
        return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), defaultSuccessMsg(msg), obj);
    }

    private static String defautlErrorMsg(String msg) {
        return StringUtils.isEmpty(msg)?errorMessage:msg;

    }

    private static String defaultSuccessMsg(String msg) {
        return StringUtils.isEmpty(msg)?successMessage:msg;
    }
}