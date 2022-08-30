package com.ljm.boot.lowcode.model.vo;

import lombok.Data;

@Data
public class PageVO<T> implements java.io.Serializable {

    private static final long serialVersionUID = 2763256922817929825L;

    private long totalElements;
    private long totalPages;
    private java.util.List<T> content;

    public PageVO(long totalElements, Integer totalPages, java.util.List<T> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public PageVO(org.springframework.data.domain.Page page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.content = page.getContent();
    }
}

