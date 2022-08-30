package com.ljm.boot.lowcode.model.vo;

import lombok.Data;

/**
 * @author Dominick Li
 * @description 下拉列表封装类
 **/
@Data
public class SelectVO implements java.io.Serializable {

    private String value;
    private String name;

    public SelectVO(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public SelectVO(Integer value, String name) {
        this.value = value.toString();
        this.name = name;
    }

    public SelectVO(Long value, String name) {
        this.value = value.toString();
        this.name = name;
    }
}
