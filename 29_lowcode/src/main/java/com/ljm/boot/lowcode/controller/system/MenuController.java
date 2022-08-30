package com.ljm.boot.lowcode.controller.system;


import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.service.system.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Dominick Li
 * @description 菜单管理
 **/
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @OperationLog(module = "菜单管理", description = "添加或修改")
    @PostMapping("/saveMenu")
    public JsonResult saveMenu(@RequestBody SysMenu sysMenu) {
        return sysMenuService.save(sysMenu);
    }

    @OperationLog(module = "菜单管理", description = "刷新菜单层级和顺序")
    @PostMapping("/reloadMenu")
    public JsonResult reloadMenu(@RequestBody java.util.List<SysMenu> sysMenus, @RequestHeader String token) {
        return sysMenuService.reloadMenu(sysMenus, token);
    }

    @OperationLog(module = "菜单管理", description = "批量删除")
    @PostMapping("/deleteByIds")
    public JsonResult deleteByIds(@RequestBody String idList) {
        return sysMenuService.deleteByIdList(JSONObject.parseArray(idList, Integer.class));
    }

    @GetMapping("/findAll")
    public JsonResult findAll() {
        return sysMenuService.findAll();
    }

}
