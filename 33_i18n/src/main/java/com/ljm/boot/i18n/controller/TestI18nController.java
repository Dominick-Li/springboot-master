package com.ljm.boot.i18n.controller;

import com.ljm.boot.i18n.enums.ReturnMessageEnum;
import com.ljm.boot.i18n.service.LoginService;
import com.ljm.boot.i18n.util.JsonResult;
import com.ljm.boot.i18n.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/8 16:48
 **/
@RestController
public class TestI18nController {

    @Autowired
    private LoginService loginService;

    /**
     * 测试国际化
     * en_US 英文  http://127.0.0.1:8033/en_US
     * zh_CN 中文 http://127.0.0.1:8033/zh_CN
     */
    @GetMapping("/{language}")
    public String test(@PathVariable String language) {
        String text1 = MessageUtil.get("test", language);
        String text2 = MessageUtil.get("test", new Object[]{language}, language);
        return new StringBuilder("没有替换占位符参数:")
                .append(text1)
                .append("<br/>")
                .append("替换占位符参数后:")
                .append(text2)
                .toString();
    }

//    /**
//     * 不优雅登录
//     */
//    @PostMapping("/login")
//    public JsonResult login(@RequestHeader String language,@RequestParam String username, @RequestParam String password) {
//        return loginService.login(language,username,password);
//    }

    /**
     * 优化后的登录接口
     */
    @PostMapping("/login")
    public JsonResult login(@RequestParam String username,@RequestParam String password) {
        return loginService.login(username,password);
    }

}
