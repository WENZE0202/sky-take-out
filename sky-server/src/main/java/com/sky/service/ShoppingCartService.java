package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * insert shopping cart item
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * list down shopping cart items by current user id from request header JWT token
     * @param userId
     * @return
     */
    List<ShoppingCart> list(Long userId);

    /**
     * subtraction one to particular item number
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);

    /**
     * clean all items from current user id
     * @param userId
     */
    void clean(Long userId);
}
