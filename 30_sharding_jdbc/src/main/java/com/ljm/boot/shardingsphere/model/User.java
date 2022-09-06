package com.ljm.boot.shardingsphere.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/9/1 23:31
 **/
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;

}
