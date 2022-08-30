package com.ljm.boot.lowcode.pointcut;

import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.service.system.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Dominick Li
 * @description 操作日志切入点
 **/
@Aspect
@Component
public class OperationLogPointcut {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 日志切入点
     */
    @Pointcut("@annotation(com.ljm.boot.lowcode.annotation.OperationLog)")
    public void operationLogPointCut() {
    }


    @AfterReturning(pointcut = "operationLogPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Method method = currentMethod(joinPoint, methodName);
        OperationLog log = method.getAnnotation(OperationLog.class);
        String module = log.module();
        String description = log.description();
        sysLogService.saveOperationLog(joinPoint, methodName, module, description);
    }


    private Method currentMethod(JoinPoint joinPoint, String methodName) {
        /**
         * 获取目标类的所有方法，找到当前要执行的方法
         */
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }


}
