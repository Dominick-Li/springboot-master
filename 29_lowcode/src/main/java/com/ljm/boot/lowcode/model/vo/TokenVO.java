package com.ljm.boot.lowcode.model.vo;

import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.system.SysUser;
import lombok.Data;

import java.util.List;


@Data
public class TokenVO {

    /**
     * 身份证认证token信息
     */
    private String token;
    private UserVo user;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 用户拥有的菜单
     */
    private List<SysMenu> menus;


    public TokenVO(String token, SysUser sysUser, String roleCode, List<SysMenu> menus) {
        this.token = token;
        this.user = new UserVo(sysUser);
        this.roleCode = roleCode;
        this.menus = menus;
    }


}
