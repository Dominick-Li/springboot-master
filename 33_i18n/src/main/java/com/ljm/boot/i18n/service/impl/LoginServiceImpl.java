package com.ljm.boot.i18n.service.impl;

import com.ljm.boot.i18n.enums.ReturnMessageEnum;
import com.ljm.boot.i18n.service.LoginService;
import com.ljm.boot.i18n.util.JsonResult;
import com.ljm.boot.i18n.util.MessageUtil;
import org.springframework.stereotype.Service;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/8/9 17:10
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public JsonResult login(String language, String username, String password) {
        if ("123456".equals(password)) {
            //通过key和语言找到对应文本的提示信息
            return JsonResult.successResult(MessageUtil.get("return.loginOk", language));
        }
        return JsonResult.failureResult(MessageUtil.get("return.pwd_error", language));
    }

    @Override
    public JsonResult login(String username, String password) {
        if ("123456".equals(password)) {
            return JsonResult.successResult(ReturnMessageEnum.LOGIN_OK);
        }
        //使用枚举类替换占位符内容后的信息
        System.out.println(ReturnMessageEnum.ACCOUNT_LOCK.setAndToString(3));
        return JsonResult.failureResult(ReturnMessageEnum.PASSWORD_ERROR);
    }
}
