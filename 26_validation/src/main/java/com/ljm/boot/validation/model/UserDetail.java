package com.ljm.boot.validation.model;

import javax.validation.constraints.*;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/5/10 12:53
 **/
public class UserDetail {

    @NotBlank
    private String address;

    @Max(60)
    @Min(18)
    @NotNull
    private Integer age;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
