package com.jj.dianpingdemo.mapper;

import com.jj.dianpingdemo.entity.SeckillVoucher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: JSY
 * @version: 1.0
 */
@Mapper
public interface SeckillVoucherMapper {

    @Insert("INSERT INTO tb_seckill_voucher(voucher_id, stock, begin_time, end_time, create_time, update_time) " +
            "VALUES(#{voucherId}, #{stock}, #{beginTime}, #{endTime}, #{createTime}, #{updateTime})")
    int insert(SeckillVoucher seckillVoucher);

    // 🔥 新增：扣减库存（基础版）
    @Update("UPDATE tb_seckill_voucher SET stock = stock - 1 WHERE voucher_id = #{voucherId} AND stock > 0")
    int deductStock(Long voucherId);

    // 🔥 新增：根据优惠券ID查询秒杀信息
    @Select("SELECT * FROM tb_seckill_voucher WHERE voucher_id = #{voucherId}")
    SeckillVoucher selectByVoucherId(Long voucherId);
}
