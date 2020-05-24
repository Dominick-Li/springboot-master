package com.ljm.databaselook.model;


import javax.persistence.*;

@Entity
@Table(name = "commodity")
public class Commodity {
    @Id
    private Integer id;
    //商品数量
    private Integer number;
    //商品名称
    private String commodityName;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }
}
