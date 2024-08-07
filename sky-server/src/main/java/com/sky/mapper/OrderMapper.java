package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * insert new order entry
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 用于替换微信支付更新数据库状态的问题
     * @param orderStatus
     * @param orderPaidStatus
     */

    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{checkOutTime} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime checkOutTime, Long id);


    /**
     * select by status and order time before
     * @param status
     * @param dateTime
     * @return
     */
    @Select(" select * from orders where status = #{status} and order_time < #{dateTime}")
    List<Orders> selectByStatusAndOrderTimeBefore(Integer status,LocalDateTime dateTime);


    /**
     * select by id
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.orders where id = #{id}")
    Orders getById(Long id);

    /**
     * take map as condition, sum amount (turnover)
     * @param map begin time, end time, status
     * @return
     */
    Long getSumByMap(Map map);

    /**
     * take map as condition, count order id
     * @param map begin time, end time, status
     * @return
     */
    Integer getCountByMap(Map map);

    /**
     * top 10 sales report within date range
     * @param beginTime
     * @param endTime
     * @param completed
     * @return
     */
    List<GoodsSalesDTO> salesTop10(@Value("beginTime") LocalDateTime beginTime,
                                   @Value("endTime") LocalDateTime endTime,
                                   @Value("completed") Integer completed);

    /**
     * List all records from orders table limit by conditions
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * Order detail select by order id (order detail, order table in used)
     * @param id
     * @return
     */
    OrderVO selectByIdWithDetail(Long id);

    /**
     * select by order id
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);
}
