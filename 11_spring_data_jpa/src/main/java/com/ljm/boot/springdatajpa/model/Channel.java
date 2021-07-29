package com.ljm.boot.springdatajpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sys_channel")
@Data
public class Channel extends BaseModel {

    //渠道名称
    private String channelName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="channelId")
    private List<User> users;
}
