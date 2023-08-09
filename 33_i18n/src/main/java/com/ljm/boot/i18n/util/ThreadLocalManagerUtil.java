package com.ljm.boot.i18n.util;

import com.ljm.boot.i18n.enums.LanguageEnum;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description 用于存储请求上下文信息
 * @CreateTime 2022/12/8 17:54
 **/
public class ThreadLocalManagerUtil {

    @Data
    public static class HeaderInfo {

        /**
         * 用户的token信息
         */
        private String token;

        /**
         * 国际化语言包名称
         */
        private String language;
    }

    /**
     * 存储请求头信息
     */
    private final static ThreadLocal<HeaderInfo> headerInfoThreadLocal = new ThreadLocal<>();

    public static void add(HeaderInfo headerInfo) {
        headerInfoThreadLocal.set(headerInfo);
    }

    public static String getToken() {
        return headerInfoThreadLocal.get().getToken();
    }

    public static String getLanguage() {
        return headerInfoThreadLocal.get() != null ? headerInfoThreadLocal.get().getLanguage() : LanguageEnum.ZH_CN.getName();
    }

    /**
     * 释放资源
     */
    public static void remove() {
        headerInfoThreadLocal.remove();
    }

}
