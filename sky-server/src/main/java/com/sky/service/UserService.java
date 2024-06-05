package com.sky.service;

import com.sky.entity.User;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;


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

    /**
     * count new user number daily and daily user history total
     * @param begin
     * @param end
     * @return
     */
    UserReportVO userReport(LocalDate begin, LocalDate end);
}
