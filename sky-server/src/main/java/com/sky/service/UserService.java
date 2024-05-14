package com.sky.service;

import com.sky.entity.User;


public interface UserService {
    /**
     * select by open id
     * @param openid
     * @return
     */
    User selectByOpenid(String openid);

    /**
     * insert
     * @param user
     */
    void insert(User user);
}
