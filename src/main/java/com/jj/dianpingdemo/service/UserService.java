package com.jj.dianpingdemo.service;

import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.mapper.UserMapper;
import org.springframework.stereotype.Service;
/**
 * @author: JSY
 * @version: 1.0
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
