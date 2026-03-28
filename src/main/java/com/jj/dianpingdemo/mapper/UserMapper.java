package com.jj.dianpingdemo.mapper;

import com.jj.dianpingdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 * @author: JSY
 * @version: 1.0
 */
// MyBatis Mapper接口，用于定义数据库操作方法
@Mapper
public interface UserMapper {

    @Select("SELECT id, phone, nick_name AS nickName FROM tb_user WHERE id = #{id}")
    User selectById(Long id);
}