package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {

    /**
     * add dish with flavors
     */
    void addWithFlavors(DishDTO dishDTO);

    /**
     * dish pagination
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Dish delete by ids
     * @param ids
     */
    void deleteByIds(List<Long> ids);
}
