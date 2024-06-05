package com.sky.service.impl;

import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public User selectByOpenid(String openid) {
        User user = userMapper.selectByOpenid(openid);
        return user;
    }


    public void insert(User user) {
        userMapper.insert(user);
    }

    /**
     * count new user number daily and daily user history total
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        // Calculate all LocalDate between begin to end
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }

        List<Integer> historyUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            // convert LocalTime to LocalDateTime format
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // count daily history user total
            // select count(id) from user where createTime < end
            Map map = new HashMap<>();
            map.put("endTime", endTime);
            Integer historyUserTotal = userMapper.getCountByMap(map);
            historyUserList.add(historyUserTotal);

            // count daily new user total
            // select count(id) from user where createTime > begin and createTime < end
            map.put("beginTime", beginTime);
            Integer newUserTotal = userMapper.getCountByMap(map);
            newUserList.add(newUserTotal);
        }

        // Match Apache E-Chart require format style: x,y,z(String)
        String localDateListStr = StringUtils.join(localDateList, ',');
        String historyUserListStr = StringUtils.join(historyUserList, ',');
        String newUserListStr = StringUtils.join(newUserList, ',');

        return UserReportVO
                .builder()
                .dateList(localDateListStr)
                .newUserList(newUserListStr)
                .totalUserList(historyUserListStr)
                .build();
    }
}
