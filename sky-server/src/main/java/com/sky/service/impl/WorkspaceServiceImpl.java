package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * Business data: turnover, valid order, order completion rate,
     *                       average order price, new users count
     * @return
     */
    public BusinessDataVO businessData(LocalDateTime beginTime, LocalDateTime endTime) {

        Map map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        // Business data calculation
        Integer ordersCount = orderMapper.getCountByMap(map); // total orders count
        Integer newUsersCount = userMapper.getCountByMap(map); // total new users count

        map.put("status", Orders.COMPLETED);
        Long temp = orderMapper.getSumByMap(map);
        Double turnover = temp == null? 0.0:(double) temp; // Avoid null return if there is no entries
        Integer validOrderCount = orderMapper.getCountByMap(map);
        Double orderCompletionRate = ordersCount == 0? 0:(double) (validOrderCount / ordersCount);
        Double unitPrice =
                validOrderCount == 0? 0:(double) (turnover / validOrderCount); // average valid order price

        return BusinessDataVO
                .builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsersCount)
                .build();
    }
}
