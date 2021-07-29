package com.ljm.boot.redissession.model;

import java.io.Serializable;

public class User implements Serializable {

    private String username;

    private String loginSessionId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(String loginSessionId) {
        this.loginSessionId = loginSessionId;
    }
}
