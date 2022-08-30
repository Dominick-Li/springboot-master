package com.ljm.boot.lowcode.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.model.system.SysRole;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.service.system.SysRoleService;
import com.ljm.boot.lowcode.service.system.impl.SysRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dominick Li
 * @description 角色操作
 **/
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private SysRoleService roleService;

    /**
     * 分页查询所有
     */
    @PostMapping("/queryByCondition")
    public JsonResult queryByCondition(@RequestBody PageParam<SysRole> pageParam) {
        return roleService.queryByCondition(pageParam);
    }

    @OperationLog(module = "角色管理", description = "批量删除")
    @PostMapping("/delete")
    public JsonResult deleteByIds(@RequestBody String idList) {
        return roleService.deleteByIdList(JSONObject.parseArray(idList, Integer.class));
    }

    @OperationLog(module = "角色管理", description = "新增或修改")
    @PostMapping("/")
    public JsonResult save(@RequestBody SysRole sysRole) {
        return roleService.save(sysRole);
    }

    /**
     * 获取添加用户需要的信息
     */
    @GetMapping("/getSelectList")
    public JsonResult getRoleAndChannel() {
        return roleService.findSelectList();
    }

    /**
     * 获取角色的菜单用于刷新
     */
    @GetMapping("/getMenuList")
    public JsonResult getMenuList(@RequestHeader String token){
        return roleService.getMenuListByToken(token);
    }

}
