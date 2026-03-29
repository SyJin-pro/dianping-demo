package com.jj.dianpingdemo.mapper;

import com.jj.dianpingdemo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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

    @Select("SELECT id, phone, nick_name AS nickName FROM tb_user WHERE phone = #{phone} LIMIT 1")
    User selectByPhone(String phone);

    @Insert("INSERT INTO tb_user(phone, nick_name) VALUES(#{phone}, #{nickName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}