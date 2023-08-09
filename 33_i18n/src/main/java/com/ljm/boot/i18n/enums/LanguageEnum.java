package com.ljm.boot.i18n.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageEnum {

    ZH_CN("zh_CN", "中文/中国"),
    EN_US( "en_US", "英语/美国"),
    ;

    /**
     * 语言_国家缩写
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

}
