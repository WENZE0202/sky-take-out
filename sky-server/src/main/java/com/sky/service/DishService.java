package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     * Dish update
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * Dish find by id
     * @param id
     * @return
     */
    DishVO selectById(Long id);
}
