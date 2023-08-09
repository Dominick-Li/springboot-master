package com.ljm.boot.i18n.service;

import com.ljm.boot.i18n.util.JsonResult;

public interface LoginService {

    JsonResult login(String language,String username,String password);

    JsonResult login(String username,String password);

}
