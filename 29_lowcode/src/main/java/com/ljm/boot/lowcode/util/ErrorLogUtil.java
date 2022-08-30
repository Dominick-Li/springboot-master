package com.ljm.boot.lowcode.util;


import com.ljm.boot.lowcode.listen.InitListen;
import com.ljm.boot.lowcode.model.system.SysErrorLog;
import com.ljm.boot.lowcode.repository.system.SysErrorLogRepository;

/**
 * @author Dominick Li
 * @description 把异常信息记录到错误日志表中
 **/
public class ErrorLogUtil {

    private ErrorLogUtil() {

    }

    public static void save(Exception e) {
        SysErrorLogRepository sysErrorLogRepository = SpringUtil.getBean(SysErrorLogRepository.class);
        String className = e.getClass().toString();
        className = className.substring(className.lastIndexOf(".") + 1);
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        SysErrorLog errorLog = new SysErrorLog(className, stackTraceElement.getFileName() + " lineNumber=" + stackTraceElement.getLineNumber(), e.toString(), InitListen.getServerIp());
        sysErrorLogRepository.save(errorLog);
    }



}
