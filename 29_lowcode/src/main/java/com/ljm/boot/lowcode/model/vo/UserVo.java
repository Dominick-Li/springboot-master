package com.ljm.boot.lowcode.model.vo;

import com.ljm.boot.lowcode.model.system.SysUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;


/**
 * @author Dominick Li
 * @CreateTime 2021/5/14 9:58
 * @description
 **/
@Data
public class UserVo {

    private Integer id;
    private Integer roleId;
    private Integer channelId;
    private String username;


    public UserVo(SysUser sysUser) {
        BeanUtils.copyProperties(sysUser, this);
    }

}
