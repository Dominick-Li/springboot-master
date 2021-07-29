package com.ljm.boot.druid.druidmonitor.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "orders")
@Data
public class Orders {
    @Id
    private String id;
    private String orderName;


}
