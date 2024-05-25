package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "Dish Related Api")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/list")
    @ApiOperation("1. Select by category id")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<Setmeal>> selectByCategoryId(Long categoryId) {
        log.info("[SELECT] Set meal table select by category id: {}", categoryId);
        List<Setmeal> list = setmealService.selectByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("2. Select by id")
    public Result<List<SetmealDish>> selectById(@PathVariable Long id){
        log.info("[SELECT] select by id, {}", id);
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO.getSetmealDishes());
    }

}
