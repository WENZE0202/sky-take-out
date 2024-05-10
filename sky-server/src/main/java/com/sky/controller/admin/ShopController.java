package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "Shop Related Api")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SHOP_STATUS = "shopStatus";

    @PutMapping("/{status}")
    @ApiOperation("1. Shop Update Status")
    public Result setStatus(@PathVariable int status){
        log.info("Current shop status: {}", (status == 1)? "Opening": "Closing");
        redisTemplate.opsForValue().set(SHOP_STATUS, status);

        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> query(){
        log.info("Shop status query starting...");
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);

        return Result.success(status);
    }
}
