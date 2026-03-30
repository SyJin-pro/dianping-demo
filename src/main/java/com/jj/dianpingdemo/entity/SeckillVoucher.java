package com.jj.dianpingdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: JSY
 * @version: 1.0
 */
@Data
public class SeckillVoucher {
    private Long voucherId;       // 对应优惠券id
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime updateTime;
}
