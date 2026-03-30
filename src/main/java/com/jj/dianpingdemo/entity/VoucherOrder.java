package com.jj.dianpingdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: JSY
 * @version: 1.0
 */
@Data
public class VoucherOrder {
    private Long id;
    private Long userId;       // 对应 user_id
    private Long voucherId;    // 对应 voucher_id
    private Integer payType;   // 对应 pay_type（默认1，无需手动赋值）
    private Integer status;    // 对应 status
    private LocalDateTime createTime;  // 数据库自动填充
    private LocalDateTime payTime;     // 对应 pay_time
    private LocalDateTime useTime;     // 对应 use_time
    private LocalDateTime refundTime;  // 对应 refund_time
    private LocalDateTime updateTime;  // 数据库自动填充
}