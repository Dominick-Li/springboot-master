package com.ljm.boot.lowcode.controller.system;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.model.system.SysErrorLog;
import com.ljm.boot.lowcode.model.system.SysLoginLog;
import com.ljm.boot.lowcode.model.system.SysOperationLog;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dominick Li
 * @description 日志管理
 **/
@RestController
@RequestMapping("/admin")
public class LogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/loginlog/queryByCondition")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult loginlogList(@RequestBody PageParam<SysLoginLog> pageParam) {
        return sysLogService.findLoginLog(pageParam);
    }

    @OperationLog(module = "登录日志", description = "批量删除")
    @PostMapping("/loginlog/deleteByIds")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult deleteLoginLogsByIds(@RequestBody String idList) {
        return sysLogService.deleteLoginlog(JSONArray.parseArray(idList, Integer.class));
    }

    @PostMapping("/operationlog/queryByCondition")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult operationlogList(@RequestBody PageParam<SysOperationLog> pageParam) {
        return sysLogService.findOperationLoginLog(pageParam);
    }

    @PostMapping("/errorlog/queryByCondition")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult errorlogList(@RequestBody PageParam<SysErrorLog> pageParam) {
        return sysLogService.findErrorLog(pageParam);
    }

    @OperationLog(module = "错误日志", description = "批量删除")
    @PostMapping("/errorlog/deleteByIds")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public JsonResult deleteErrorLogsByIds(@RequestBody String idList) {
        return sysLogService.deleteErrorlog(JSONObject.parseArray(idList, Integer.class));
    }


}
