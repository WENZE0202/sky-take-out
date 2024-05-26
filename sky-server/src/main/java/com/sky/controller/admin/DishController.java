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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "Dish Related API")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * add dish with flavors
     * @return
     */
    @PostMapping
    @ApiOperation(value = "1. Dish Insert With Flavors")
    @CacheEvict(cacheNames = "dishCache", key = "#dishDTO.categoryId")
    public Result addWithFlavors(@RequestBody DishDTO dishDTO){
        log.info("[INSERT] Insert dish with flavors from: {}", dishDTO);
        dishService.addWithFlavors(dishDTO);

        // Clean cache by key start with Dish_
        //String pattern = "Dish_" + dishDTO.getCategoryId();
        //deleteKey(pattern);
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
        log.info("[PAGE] pagination, {}", dishPageQueryDTO);
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
    @CacheEvict(cacheNames = "dishCahce", allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        log.info("[DELECT] batch delete by ids: {}", ids);
        dishService.deleteByIds(ids);

        // Clean cache by key start with Dish_
        //deleteKey("Dish_*");
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
        log.info("[SELECT] dish find by id: {}", id);
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
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("[UPDATE] dish update: {}", dishDTO);
        dishService.update(dishDTO);

        // Clean cache by key start with Dish_
        // deleteKey("Dish_*");
        return Result.success();
    }


    /**
     * select by category id(cooperate with set-meal form business logic)
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("6. select by category id")
    public Result<List<DishVO>> selectByCategoryId(Long categoryId){
        log.info("[SELECT] dish select by category id: {}", categoryId);
        List<DishVO> list = dishService.selectByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * Redis ops: delete by key pattern
     * @param pattern
     * 25/5/2024 change to Spring Cache
     */
    private void deleteKey(String pattern){
        Set keys = redisTemplate.keys(pattern);
        if(keys != null && keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

}
