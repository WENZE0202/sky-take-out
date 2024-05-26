package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    public void add(ShoppingCartDTO shoppingCartDTO) {
        // check shopping cart table by dishId, set-mealId and userId
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, cart);
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(cart);

        if(list != null && list.size() > 0){
            // list NOT null: duplicate entry, update number by add 1
            ShoppingCart shoppingCart = list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateByNumber(shoppingCart);
        }else {
            // list EQUAL to null: no entries in table, Insert new entry into shopping cart
            Long dishId = cart.getDishId();
            if(dishId != null){
                // Dish operation: insert dish record by its id
                Dish dish = dishMapper.selectById(dishId);
                cart.setAmount(dish.getPrice());
                cart.setImage(dish.getImage());
                cart.setName(dish.getName());
            }else {
                // Set-meal operation: insert set-meal record by its id
                Long setmealId = cart.getSetmealId();
                Setmeal setmeal = setMealMapper.selectById(setmealId);
                cart.setAmount(setmeal.getPrice());
                cart.setImage(setmeal.getImage());
                cart.setName(setmeal.getName());
            }
            cart.setCreateTime(LocalDateTime.now());
            cart.setNumber(1);
            shoppingCartMapper.add(cart);
        }

    }


    public List<ShoppingCart> list(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }


    public void sub(ShoppingCartDTO shoppingCartDTO) {
        // 1. find the selected item by conditions
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 2. Only one item will be found, duplicate will combine and increase item number
        if (list != null && list.size() > 0){
            // 3. sub one number
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateByNumber(cart);
        }
    }

    @Override
    public void clean(Long userId) {
        shoppingCartMapper.clean(userId);
    }
}
