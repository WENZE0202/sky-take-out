package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * update order status to cancel where order time lesser than current time minus 15 minute
     * in one minutes
     */
    @Scheduled(cron = "0 * * * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void cancelOrderTask(){
        log.info("[SCHEDULED] Cancel order task trigger");

        //sql: select * from order where status = ? and order_time < (current time - 15 minutes)
        Integer status = Orders.PENDING_PAYMENT;
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList =
                orderMapper.selectByStatusAndOrderTimeBefore(status, dateTime);

        // update status to cancel for meets condition entries after query
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单超时");
                orderMapper.update(orders);
            }
        }

    }

    /**
     * update status to completed where order status delivery in progress in 1am everyday
     */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void completedOrderTask(){
        log.info("[SCHEDULED] Completed order task trigger");

        // select * from orders where status = ? and order_time < (current time - 1 hours)
        Integer status = Orders.DELIVERY_IN_PROGRESS;
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.selectByStatusAndOrderTimeBefore(status, dateTime);

        // update status to completed for meets condition entries after query
        if (ordersList != null && ordersList.size() > 0){
            for (Orders order : ordersList) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
