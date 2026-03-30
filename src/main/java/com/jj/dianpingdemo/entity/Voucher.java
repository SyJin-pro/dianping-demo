package com.jj.dianpingdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: JSY
 * @version: 1.0
 */
@Data
public class Voucher {
    private Long id;
    private Long shopId;
    private String title;
    private String subTitle;
    private String rules;
    private Long payValue;
    private Long actualValue;
    private Integer type;   // 0普通券 1秒杀券
    private Integer status; // 1上架 0下架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
