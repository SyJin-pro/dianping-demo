package com.jj.dianpingdemo.mapper;

import com.jj.dianpingdemo.entity.Voucher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author: JSY
 * @version: 1.0
 */
@Mapper
public interface VoucherMapper {

    @Insert("INSERT INTO tb_voucher(shop_id,title,sub_title,rules,pay_value,actual_value,type,status,create_time,update_time) " +
            "VALUES(#{shopId},#{title},#{subTitle},#{rules},#{payValue},#{actualValue},#{type},#{status},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Voucher voucher);
}
