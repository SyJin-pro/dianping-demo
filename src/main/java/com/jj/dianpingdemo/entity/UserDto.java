package com.jj.dianpingdemo.entity;

/**
 * @author: JSY
 * @version: 1.0
 */

import lombok.Data;

/**
 * 对外返回的用户信息（脱敏）
 */
@Data
public class UserDto {
    private Long id;
    private String phone;
    private String nickName;
}
