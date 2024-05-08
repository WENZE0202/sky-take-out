package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Autowired
    private SetMealMapper setMealMapper;


    @Override
    @Transactional
    public void addWithFlavors(DishDTO dishDTO) {

        //1. dish table
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        Long id = dish.getId();

        //2. flavor table
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            flavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        // page helper assists in LIMIT query interpolation
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 1. status NOT enable
        for (Long id: ids) {
            Dish dish = dishMapper.selectById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2. set-meal didn't contain dish id
        List<Setmeal> setmeals = setMealMapper.selectByDishIds(ids);
        if (setmeals != null && setmeals.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 3. delete ids from dish table
        dishMapper.deleteByIds(ids);

        // 4. delete ids from flavor table
        flavorMapper.deleteByDishIds(ids);
    }


    @Override
    public DishVO selectById(Long id) {
        // 1. dish table
        Dish dish = dishMapper.selectById(id);

        // 2. flavor table
        List<DishFlavor> flavors = flavorMapper.selectById(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Transactional
    public void update(DishDTO dishDTO) {
        // 1. update dish table
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 2. update flavor table
        // 2.1 delete flavors by id
        List<Long> ids = new ArrayList<>();
        ids.add(dishDTO.getId());
        flavorMapper.deleteByDishIds(ids);

        // 2.2 insert flavor
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            flavorMapper.insertBatch(flavors);
        }
    }
}
