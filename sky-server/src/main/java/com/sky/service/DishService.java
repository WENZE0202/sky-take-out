package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {

    /**
     * add dish with flavors
     */
    public void addWithFlavors(DishDTO dishDTO);
}
