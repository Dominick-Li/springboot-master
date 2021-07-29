package com.ljm.boot.simplifycode.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;
import java.util.Date;



public  class BaseModel implements Serializable {

    private static final long serialVersionUID = 2863256929817929825L;
    private Integer id;
    private Date createTime;
    private Date lastmodifiedTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastmodifiedTime() {
        return lastmodifiedTime;
    }

    public void setLastmodifiedTime(Date lastmodifiedTime) {
        this.lastmodifiedTime = lastmodifiedTime;
    }

    /**
     * 把对象转换成json字符
     * 默认toString方法打印的是对象内存地址
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
