package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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


    /**
     * dish with flavors find by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("4. Dish find by id")
    public Result<DishVO> selectById(@PathVariable Long id){
        log.info("dish find by id: {}", id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * dish update
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("5. Dish update")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("dish update: {}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }


    /**
     * select by category id(cooperate with set-meal form business logic)
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("6. select by category id")
    public Result<List<Dish>> selectByCategoryId(Long categoryId){
        log.info("dish select by category id: {}", categoryId);
        List<Dish> list = dishService.selectByCategoryId(categoryId);
        return Result.success(list);
    }

}
