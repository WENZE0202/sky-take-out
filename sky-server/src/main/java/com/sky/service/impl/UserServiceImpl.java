package com.sky.service.impl;

import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByOpenid(String openid) {
        User user = userMapper.selectByOpenid(openid);
        return user;
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }
}
