package com.ljm.boot.lowcode.model.dto;

import lombok.Data;

/**
 * @author Dominick Li
 * @description 修改密码
 **/
@Data
public class UpdatePasswordDTO {
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 原密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String password;

}
