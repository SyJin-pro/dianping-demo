package com.jj.dianpingdemo.entity;

import java.io.Serializable;

/**
 * @author: JSY
 * @version: 1.0
 */
// 用户实体类
public class User implements Serializable {
    private Long id;
    private String phone;
    private String nickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
