package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "Shop Related Api")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SHOP_STATUS = "shopStatus";


    @GetMapping("/status")
    public Result<Integer> query(){
        log.info("Shop status query starting...");
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);

        return Result.success(status);
    }
}
