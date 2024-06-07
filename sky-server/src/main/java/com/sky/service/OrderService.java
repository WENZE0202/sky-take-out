package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.*;

import java.time.LocalDate;

public interface OrderService {

    /**
     *  submit order detail into order&order_detail table
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 客户催单
     * @param id
     */
    void remind(Long id);

    /**
     * turnover statistic: sum every entry valid amount within given date time interval
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * daily effective order and total order, count and rate statistics
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO orderReport(LocalDate begin, LocalDate end);

    /**
     * top 10 sales report within date range
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end);

    /**
     * List all records from orders table limit by conditions
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * Order detail select by order id (order detail, order table in used)
     * @param id
     * @return
     */
    OrderVO detailSelectById(Long id);


    /**
     * update order status in difference scenario
     * @param orderStatusDTO
     */
    void updateStatus(OrderStatusDTO orderStatusDTO);
}
