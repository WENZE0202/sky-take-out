package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
