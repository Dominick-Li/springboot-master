package com.ljm.boot.lowcode.controller.system;


import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.model.dto.UpdatePasswordDTO;
import com.ljm.boot.lowcode.model.system.SysUser;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.service.system.SysUserService;
import com.ljm.boot.lowcode.util.IpAddrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dominick Li
 * @description 用户管理
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/queryByCondition")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult queryByCondition(@RequestBody PageParam<SysUser> pageParam) {
        return sysUserService.queryByCondition(pageParam);
    }

    @OperationLog(module = "用户管理", description = "禁用或者启用")
    @PostMapping("/userEnabled")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult userEnabled(@RequestBody String data) {
        return sysUserService.userEnabled(data);
    }

    @OperationLog(module = "用户管理", description = "删除")
    @PostMapping("/deleteByIds")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult deleteUsersByIds(@RequestBody String idList) {
        return sysUserService.deleteByIdList(JSONObject.parseArray(idList,Integer.class));
    }

    @OperationLog(module = "用户管理", description = "添加或修改")
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }

    @OperationLog(module = "用户管理", description = "重置密码")
    @GetMapping("/restPassword/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult restPassword(@PathVariable Integer id) {
        return sysUserService.restPassword(id);
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public JsonResult updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return sysUserService.updatePassword(updatePasswordDTO);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public JsonResult login(@RequestBody String data, HttpServletRequest request) {
        return sysUserService.login(data, IpAddrUtil.getLocalIp(request));
    }

}
