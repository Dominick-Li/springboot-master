package com.ljm.boot.lowcode.service.system;

import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.service.BaseService;

/**
 * @author MCQ
 * @Description
 * @date 2021-04-15 14:53
 */
public interface SysMenuService extends BaseService<SysMenu, Integer> {

    JsonResult findAll();

    JsonResult reloadMenu(java.util.List<SysMenu> sysMenus, String token);

}
