package com.ljm.boot.lowcode.service.system;


import com.ljm.boot.lowcode.model.system.SysErrorLog;
import com.ljm.boot.lowcode.model.system.SysLoginLog;
import com.ljm.boot.lowcode.model.system.SysOperationLog;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import org.aspectj.lang.JoinPoint;

/**
 * @author Dominick Li
 * @CreateTime 2020/4/22 15:02
 * @description 日志管理
 **/
public interface SysLogService {

    /**
     * 查看登录日志
     */
    JsonResult findLoginLog(PageParam<SysLoginLog> pageParam);

    /**
     * 删除登录日志
     */
    JsonResult deleteLoginlog(java.util.List<Integer> idList);

    /**
     * 保存操作日志
     */
    void saveOperationLog(JoinPoint joinPoint, String methodName, String module, String description);

    /**
     * 查看操作日志
     */
    JsonResult findOperationLoginLog(PageParam<SysOperationLog> pageParam);

    /**
     * 查看错误日志
     */
    JsonResult findErrorLog(PageParam<SysErrorLog> pageParam);

    /**
     * 删除错误日志
     */
    JsonResult deleteErrorlog(java.util.List<Integer> idList);

}
