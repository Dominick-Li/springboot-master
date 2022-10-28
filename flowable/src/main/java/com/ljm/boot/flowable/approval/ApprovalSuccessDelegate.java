package com.ljm.boot.flowable.approval;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/10/28 10:25
 **/
public class ApprovalSuccessDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("审批通过了");
    }
}
