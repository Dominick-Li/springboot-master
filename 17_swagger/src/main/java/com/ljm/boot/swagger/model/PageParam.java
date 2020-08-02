package com.ljm.boot.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;


@ApiModel("分页查询条件封装类")
public class PageParam {

    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("每页展示多少条数据")
    private Integer pageSize;

    @ApiModelProperty("查询条件")
    private HashMap<String,String> condition;


    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public HashMap<String, String> getCondition() {
        return condition;
    }

    public void setCondition(HashMap<String, String> condition) {
        this.condition = condition;
    }
}
