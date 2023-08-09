package com.ljm.boot.i18n.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljm.boot.i18n.enums.ReturnMessageEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author Dominick Li
 * @CreateTime 2021/10/9 14:05
 * @description 返回数据封装
 **/
@Data
public class JsonResult<T> implements Serializable {

    /**
     * 成功标识 200成功，其它异常
     */
    private int code = 200;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private static final long serialVersionUID = -7268040542410707954L;


    public JsonResult() {
    }

    public JsonResult(int code) {
        this.setCode(code);
    }

    public JsonResult(int code, String msg) {
        this(code);
        this.setMsg(msg);
    }

    public JsonResult(int code, String msg, T data) {
        this(code, msg);
        this.setData(data);
    }

    public static JsonResult build(boolean flag) {
        return new JsonResult(flag ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(), flag ? ReturnMessageEnum.OK.toString() : ReturnMessageEnum.FAILED.toString());
    }

    public static JsonResult successResult() {
        return new JsonResult(HttpStatus.OK.value(), ReturnMessageEnum.OK.toString());
    }

    public static JsonResult successResult(String msg) {
        return new JsonResult(HttpStatus.OK.value(), defaultSuccessMsg(msg));
    }


    public static <T> JsonResult<T> successResult(T obj) {
        return new JsonResult(HttpStatus.OK.value(), ReturnMessageEnum.OK.toString(), obj);
    }

    public static <T> JsonResult<T> successResult(String msg, T obj) {
        return new JsonResult(HttpStatus.OK.value(), defaultSuccessMsg(msg), obj);
    }


    public static JsonResult failureResult() {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), ReturnMessageEnum.FAILED.toString());
    }

    public static JsonResult failureResult(String msg) {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), defautlErrorMsg(msg));
    }

    public static JsonResult failureResult(ReturnMessageEnum returnMessageEnum) {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), returnMessageEnum.toString());
    }


    protected static String defautlErrorMsg(String msg) {
        if (!StringUtils.isEmpty(msg)) {
            return msg;
        } else {
            return ReturnMessageEnum.FAILED.toString();
        }
    }

    protected static String defaultSuccessMsg(String msg) {
        if (!StringUtils.isEmpty(msg)) {
            return msg;
        } else {
            return ReturnMessageEnum.OK.toString();
        }
    }
}
