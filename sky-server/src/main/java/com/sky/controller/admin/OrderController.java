package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;

import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "Order API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * List all records from orders table limit by conditions
     * @param ordersPageQueryDTO conditions
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("1. List all by condition")
    public Result<PageResult> list(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("[PAGE] List all records from orders table limit by conditions: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.page(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
}
