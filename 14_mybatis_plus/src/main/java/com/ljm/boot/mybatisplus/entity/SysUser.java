package com.ljm.boot.mybatisplus.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LeeJunMing
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends  BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private int grade;



}
