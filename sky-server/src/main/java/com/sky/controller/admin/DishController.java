package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "Dish Related API")
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * add dish with flavors
     * @return
     */
    @PostMapping
    @ApiOperation(value = "1. Dish Insert With Flavors")
    public Result addWithFlavors(@RequestBody DishDTO dishDTO){
        log.info("Insert dish with flavors: {}", dishDTO);
        dishService.addWithFlavors(dishDTO);

        return Result.success();
    }

    /**
     * Dish pagination
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("2. Dish pagination")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish pagination, {}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Dish delete by ids
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("3. Dish delete by ids")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Dish delete by ids: {}", ids);
        dishService.deleteByIds(ids);
        return Result.success();
    }

}
