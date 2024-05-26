package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "Shopping Cart Related")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * insert shopping cart item IF there don't have record in table
     * OR increase one number from particular item IF there have record
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("1. Add item/ update item number")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("[INSERT] Add shopping cart item: {}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * list down shopping cart items by current user id from request header JWT token
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("2. List down items")
    public Result<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        log.info("[LIST] list down shopping cart items by user id: {}", userId);
        List<ShoppingCart> list = shoppingCartService.list(userId);
        return Result.success(list);
    }

    /**
     * subtraction one to particular item number
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("3. Sub item number")
    public Result subNumber(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("[UPDATE] subtraction quantity by conditions: {}", shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }

    /**
     * clean all items from current user id
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("4. clean all items")
    public Result clean(){
        Long userId = BaseContext.getCurrentId();
        log.info("[DELETE] clean all shopping cart items by user id: {}", userId);
        shoppingCartService.clean(userId);
        return Result.success();
    }
}
