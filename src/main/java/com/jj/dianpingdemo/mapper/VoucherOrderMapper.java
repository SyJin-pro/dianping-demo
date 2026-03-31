package com.jj.dianpingdemo.mapper;

import com.jj.dianpingdemo.entity.VoucherOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: JSY
 * @version: 1.0
 */
@Mapper
public interface VoucherOrderMapper {

    // 插入订单（只传必填字段，其余用数据库默认值）
    @Insert("INSERT INTO tb_voucher_order(id, user_id, voucher_id, status) " +
            "VALUES(#{id}, #{userId}, #{voucherId}, #{status})")
    int insert(VoucherOrder order);

    // 校验一人一单（逻辑不变）
//    @Select("SELECT id FROM tb_voucher_order WHERE user_id = #{userId} AND voucher_id = #{voucherId}")
//    Long existsByUserIdAndVoucherId(@Param("userId") Long userId, @Param("voucherId") Long voucherId);
    @Select("SELECT COUNT(*) FROM tb_voucher_order WHERE user_id = #{userId} AND voucher_id = #{voucherId}")
    int countByUserIdAndVoucherId(Long userId, Long voucherId);
}
