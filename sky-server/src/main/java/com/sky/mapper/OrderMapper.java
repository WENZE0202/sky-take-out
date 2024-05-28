package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * insert new order entry
     * @param orders
     */
    void insert(Orders orders);
}
