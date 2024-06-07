package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.enumeration.OrderStatus;
import com.sky.result.PageResult;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.dto.OrderStatusDTO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Order detail select by order id (order detail, order table in used)
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("2. Order detail")
    public Result<OrderVO> detail(@PathVariable("id") Long id){
        log.info("[GET] Order detail id: {}", id);
        OrderVO orderVO = orderService.detailSelectById(id);
        return Result.success(orderVO);
    }

    /**
     * update order status to confirm
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("3. update status to accept")
    public Result confirm(@RequestBody OrderStatusDTO orderStatusDTO){
        log.info("[UPDATE] update order status to confirm, id: {}", orderStatusDTO);
        orderStatusDTO.setOrderStatus(OrderStatus.CONFIRMED);
        orderService.updateStatus(orderStatusDTO);
        return Result.success();
    }


    /**
     * update order status to reject
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("4. update status to reject")
    public Result rejection(@RequestBody OrderStatusDTO orderStatusDTO){
        log.info("[UPDATE] update order status to reject, detail {}", orderStatusDTO);
        orderStatusDTO.setOrderStatus(OrderStatus.CANCELLED);
        orderService.updateStatus(orderStatusDTO);
        return Result.success();
    }

    /**
     * update order status to confirm
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("5. update status to delivery in progress")
    public Result delivery(@PathVariable("id") Long id){
        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        orderStatusDTO.setId(id);
        log.info("[UPDATE] update order status to delivery, id: {}", orderStatusDTO);
        orderStatusDTO.setOrderStatus(OrderStatus.DELIVERY_IN_PROGRESS);
        orderService.updateStatus(orderStatusDTO);
        return Result.success();
    }

    /**
     * update order status to confirm
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("6. update status to complete")
    public Result complete(@PathVariable("id") Long id){
        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        orderStatusDTO.setId(id);
        log.info("[UPDATE] update order status to complete, id: {}", orderStatusDTO);
        orderStatusDTO.setOrderStatus(OrderStatus.COMPLETED);
        orderService.updateStatus(orderStatusDTO);
        return Result.success();
    }

    /**
     * update order status to reject
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("7. update status to cancel")
    public Result cancel(@RequestBody OrderStatusDTO orderStatusDTO){
        log.info("[UPDATE] update order status to cancel, detail {}", orderStatusDTO);
        orderStatusDTO.setOrderStatus(OrderStatus.CANCELLED);
        orderService.updateStatus(orderStatusDTO);
        return Result.success();
    }

}
