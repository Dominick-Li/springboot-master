package com.ljm.boot.mybatisplus.entity.page;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public class PageResult<T> {

    private List<T> records;

    private long totalRecord;

    private long totalPage;


    public PageResult(){ }

    public PageResult(IPage<T> iPage){
        this.records=iPage.getRecords();
        this.totalPage=iPage.getPages();
        this.totalRecord=iPage.getTotal();
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
