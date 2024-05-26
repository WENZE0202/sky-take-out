package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "Dish Related Api")
public class DishController {

    @Autowired
    private DishService dishService;


    @GetMapping("/list")
    @ApiOperation("1. Select by category id")
    @Cacheable(cacheNames = "dishCache", key = "#categoryId")
    public Result<List<DishVO>> selectByCategoryId(Long categoryId){
        log.info("[SELECT] select by category id: {}", categoryId);
        List<DishVO> list = dishService.selectByCategoryId(categoryId);
        return Result.success(list);
    }
}
