package com.ljm.boot.lowcode.enums;

/**
 * @Author Dominick Li
 * @Description 以mysql数据库的字段类型为例
 **/
public enum DataTypeEnum {

    INT("int", "整型"),
    LONG("bigint", "长整型"),
    STRING("varchar(255)", "字符串"),
    BOOLEAN("bit", "布尔类型"),
    DATE("datetime","日期类型"),
    TEXT("text", "文本块"),
    MEDIUM_TEXT("mediumtext", "大文本块"),
    ;

    DataTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
