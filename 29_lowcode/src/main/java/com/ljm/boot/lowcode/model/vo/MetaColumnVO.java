package com.ljm.boot.lowcode.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljm.boot.lowcode.model.MetaColumn;
import lombok.Data;

/**
 * @Author Dominick Li
 * @CreateTime 2022/6/12 19:41
 * @Description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaColumnVO {

    /**
     * 列名称
     */
    private String name;

    /**
     * 字段编码
     */
    private String code;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 是否展示
     */
     private boolean viewShow;

    /**
     * 是否作为查询条件
     */
    private boolean search;

    /**
     * 字段内容
     */
    private String value;


    public MetaColumnVO(){}

    public MetaColumnVO(MetaColumn metaColumn) {
        this.name = metaColumn.getColumnName();
        this.code = metaColumn.getColumnCode();
        this.dataType=metaColumn.getDataType();
        this.viewShow=metaColumn.isViewShow();
        this.search=metaColumn.isSearch();
    }
}
