package com.ljm.boot.mybatisplus.entity.page;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Dominick Li
 * @CreateTime 2020/3/20 15:45
 * @description
 **/
@Data
public class PageParam<T>  {

    private Integer DEFAULT_PAGE_SIZE=10;

    private Page<T> page;

    //查询条件构造器
    private QueryWrapper<T> queryWrapper;

    private Integer currentPage;

    private Integer pageSize;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private Date beginDate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private Date endDate;

    private Map<String, Object> eq = new HashMap<>();

    private Map<String, String> like = new HashMap<>();

    /**
     * 构建jpa需要的分页对象 初始化
     */
    public  void initialization() {
        if(this.currentPage==null){
            this.currentPage=1;
        }
        if(this.pageSize==null){
            this.pageSize=this.DEFAULT_PAGE_SIZE;
        }
        this.page= new Page<>(this.currentPage, this.pageSize);
        this.setCompare();
    }


    /**
     * 注入查询条件参数
     */
    private void setCompare() {
        this.queryWrapper=new QueryWrapper<>();
        if (!StringUtils.isEmpty(beginDate)) {
            //大于或等于传入时间
            queryWrapper.ge("create_time",beginDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            //小于或等于传入时间
            queryWrapper.le("create_time",beginDate);
        }

        String value = "";
        //注入文本框的模糊查询参数
        for (Map.Entry<String, String> entry : like.entrySet()) {
            value = entry.getValue().trim();
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            //过滤掉非法的符号,不然会影响模糊查询
            value = value.replaceAll("%", "////%").replaceAll("_", "////_");
            queryWrapper.like(entry.getKey(),  value );
        }

        //注入下拉框或单选框的等值查询参数
        for (Map.Entry<String, Object> entry : eq.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            queryWrapper.eq(entry.getKey(),entry.getValue());
        }
    }
}
