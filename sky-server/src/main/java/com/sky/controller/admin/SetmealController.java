package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "Setmeal Related Api")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * set meal page query
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("1. page query")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("[PAGE] set meal page query: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    @PostMapping()
    @ApiOperation("2. insert")
    public Result insert(@RequestBody SetmealDTO setmealDTO){
        log.info("[INSERT] set meal insert from: {}", setmealDTO);
        setmealService.insert(setmealDTO);

        // Clean cache by key start with Dish_
        String pattern = "Dish_" + setmealDTO.getCategoryId();
        deleteKey(pattern);
        return Result.success();
    }

    @RequestMapping("/status/{status}")
    @ApiOperation("3. status start or stop")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("[UPDATE] set meal id {} status: {}", id, (status == 0? "disable": "enable"));
        setmealService.startOrStop(status, id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("4. batch delete by ids")
    public Result deleteBatch(@RequestParam Long[] ids){
        log.info("[DELETE] batch delete from ids: {}", ids);
        setmealService.deleteBatch(ids);

        // Clean cache by key start with Dish_
        deleteKey("Dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("5. select by id [UPDATE purpose]")
    public Result<SetmealVO> selectById(@PathVariable Long id){
        log.info("[SELECT] select by id: {}", id);
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO);
    }


    @PutMapping
    @ApiOperation("6. update")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("[UPDATE] update: {}", setmealDTO);
        setmealService.update(setmealDTO);

        // Clean cache by key start with Dish_
        deleteKey("Dish_*");
        return Result.success();
    }

    /**
     * Redis ops: delete by key pattern
     * @param pattern
     */
    private void deleteKey(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

}
