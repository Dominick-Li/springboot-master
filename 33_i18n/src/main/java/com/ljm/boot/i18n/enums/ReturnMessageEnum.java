package com.ljm.boot.i18n.enums;

import com.ljm.boot.i18n.util.MessageUtil;
import com.ljm.boot.i18n.util.ThreadLocalManagerUtil;
import lombok.Getter;

@Getter
public enum ReturnMessageEnum {

    OK("ok"),
    FAILED("failed"),
    LOGIN_OK("loginOk"),
    PASSWORD_ERROR("pwd_error"),
    ACCOUNT_LOCK("account_lock"),
    ;
    /**
     * 国际化信息文件里的Key前缀
     */
    private final static String prefix = "return.";

    /**
     * 返回的国际化key信息
     */
    private String key;


    ReturnMessageEnum(String key) {
        this.key = prefix + key;
    }

    @Override
    public String toString() {
        return MessageUtil.get(key, ThreadLocalManagerUtil.getLanguage());
    }

    /**
     * 替换占位符中的参数
     *
     * @param params 需要替换的参数值 长度可变
     */
    public String setAndToString(Object... params) {
        return MessageUtil.get(key, params, ThreadLocalManagerUtil.getLanguage());
    }

}
