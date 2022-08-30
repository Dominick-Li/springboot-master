package com.ljm.boot.lowcode.model.tool;


import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author Dominick Li
 * @description 返回数据封装
 **/
@Data
public class JsonResult<T>  implements java.io.Serializable {

    /**
     * 成功标识 200成功，其它异常
     */
    private int code=200;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private String timestamp;

    private static final long serialVersionUID = -7268040542410707954L;

    protected static String successMessage = "操作成功";

    protected static String errorMessage = "操作失败";

    public JsonResult() {
    }

    public JsonResult(int code) {
        this.setCode(code);
        this.setTimestamp(String.valueOf(System.currentTimeMillis()));
    }

    public JsonResult(int code, String msg) {
        this(code);
        this.setMsg(msg);
    }

    public JsonResult(int code, String msg, T data) {
        this(code, msg);
        this.setData(data);
    }

    public static JsonResult successResult() {
        return new JsonResult(HttpStatus.OK.value(), successMessage);
    }

    public static JsonResult successResult(String msg) {
        return new JsonResult(HttpStatus.OK.value(), defaultSuccessMsg(msg));
    }


    public static <T> JsonResult<T> successResult(T obj) {
        return new JsonResult(HttpStatus.OK.value(), successMessage, obj);
    }


    public static  <T> JsonResult<T> successResult(String msg, T obj) {
        return new JsonResult(HttpStatus.OK.value(), defaultSuccessMsg(msg), obj);
    }


    public static JsonResult failureResult() {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
    }

    public static JsonResult failureResult(Integer code, String msg) {
        return new JsonResult(code, defautlErrorMsg(msg));
    }

    public static JsonResult failureResult(Integer code, String msg, Object data) {
        return new JsonResult(code, defautlErrorMsg(msg), data);
    }

    public static JsonResult failureResult(String msg) {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), defautlErrorMsg(msg));
    }

    public static JsonResult failureResult(Object data) {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, data);
    }

    protected static String defautlErrorMsg(String msg) {
        if (msg == null || msg.length() <= 0) {
            return errorMessage;
        } else {
            return msg;
        }
    }

    protected static String defaultSuccessMsg(String msg) {
        if (msg == null || msg.length() <= 0) {
            return successMessage;
        } else {
            return msg;
        }
    }
}
