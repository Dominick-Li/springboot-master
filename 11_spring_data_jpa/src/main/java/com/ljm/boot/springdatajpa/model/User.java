package com.ljm.boot.springdatajpa.model;

import com.sun.istack.NotNull;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sys_user",indexes = {
        @Index(name = "address_age",  columnList = "address,age")
})
@Data
public class User  extends  BaseModel{

    @Column(name = "username", unique = true, nullable = false,updatable = false,length = 36)
    private String username;

    @Column(name = "password", nullable = false, length = 36)
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 36)
    private String email;

    @Column(name = "mobile", unique = true, nullable = false, length = 11)
    private String mobile;

    @Column(name = "address",length = 64)
    private String address;

    @NotNull
    private Integer age;

    private Integer roleId;

    private Integer channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="channelId",referencedColumnName="id",insertable = false, updatable = false)
    private Channel channel;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roleId",referencedColumnName="id",insertable = false, updatable = false)
    private Role role;


    /**
     * 该字段在映射的时候会被忽略
     */
    @Transient
    private String ignoreColumn;

    /**
     * 用户拥有的角色信息
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role", joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
}
