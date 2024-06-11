package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // Business Exception troubleshooting
        // ex1: Address must not NULL
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // ex2: Shopping cart must not NULL
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (!(shoppingCartList != null && shoppingCartList.size() > 0)){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // insert into order table
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setAddress(addressBook.getDetail());
        orders.setOrderTime(LocalDateTime.now());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(BaseContext.getCurrentId());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setStatus(Orders.PENDING_PAYMENT);

        orderMapper.insert(orders);

        // insert batch entries into order_detail table
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // clean shopping cart items
        shoppingCartMapper.clean(BaseContext.getCurrentId());

        // encapsulate VO object
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .orderTime(orders.getOrderTime())
                .orderAmount(ordersSubmitDTO.getAmount())
                .orderNumber(orders.getNumber())
                .id(orders.getId())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );

        // [Temporary code] WeChat pay feature cant accomplish due to don't have official merchant entity
        JSONObject jsonObject = new JSONObject();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomNumeric(32);
        jsonObject.put("timeStamp", timeStamp);
        jsonObject.put("nonceStr", nonceStr);
        jsonObject.put("code", "ORDERPAID");
        // *************************************

//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        // update order status
        paySuccess(ordersPaymentDTO.getOrderNumber());

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        // push incoming order notify to all user session
        Map map = new HashMap<>();
        map.put("type", 1); // 1: incoming order notify 2: client order reminder
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号：" + outTradeNo);
        String jsonString = JSONObject.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);

    }

    /**
     * 客户催单
     * @param id
     */
    public void remind(Long id) {
        Orders orders = orderMapper.getById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 2); // 1: incoming order notify 2: customer order reminder
        jsonObject.put("orderId", id);
        jsonObject.put("content", "订单号： " + orders.getNumber());
        String jsonString = jsonObject.toJSONString();

        // notify all connection session(client)
        webSocketServer.sendToAllClient(jsonString);
    }


    /**
     * turnover statistic: sum every entry valid amount within given date time interval
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // find out every date start with begin until end
        List<LocalDate> localDateList = findAllDateBetween(begin, end);

        List<Long> turnoverList = new ArrayList<>();
        // records local date list turnover amount
        for (LocalDate date : localDateList) {
            // select sum(amount) turnover from orders where order_time > ? and order_time < ? and status = ?
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            Long turnover = orderMapper.getSumByMap(map);
            turnover = turnover == null? (long) 0.0 : turnover;
            turnoverList.add(turnover);
        }

        // convert list to string, split by comma
        String dateStr = StringUtils.join(localDateList, ',');
        String turnoverStr = StringUtils.join(turnoverList, ',');

        return TurnoverReportVO
                .builder()
                .dateList(dateStr)
                .turnoverList(turnoverStr)
                .build();
    }

    /**
     * daily effective order and total order, count and rate statistics
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        // find out every date start with begin until end
        List<LocalDate> localDateList = findAllDateBetween(begin, end);

        List<Integer> dailyTotalOrderCountList = new ArrayList<>();
        List<Integer> dailyEffectiveOrderCountList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // Daily total order count
            // select count(id) from orders where create_time > ? and create_time < ?
            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Integer countTotal = orderMapper.getCountByMap(map);
            dailyTotalOrderCountList.add(countTotal);

            // Daily effective order count
            // select count(id) from orders where status = ? and create_time > ? and create_time < ?
            map.put("status", Orders.COMPLETED);
            Integer countValid = orderMapper.getCountByMap(map);
            dailyEffectiveOrderCountList.add(countValid);
        }

        // convert list to string, split by comma
        String dateListStr = StringUtils.join(localDateList, ',');
        String dailyTotalOrderCountListStr = StringUtils.join(dailyTotalOrderCountList, ',');
        String dailyEffectiveOrderCountListStr = StringUtils.join(dailyEffectiveOrderCountList, ',');

        // Daily effective order/ total order overall date range sum
        // JDK8: streams handle collection data type processing
        Integer sumTotalOrder = dailyTotalOrderCountList.stream().reduce(Integer::sum).get();
        Integer sumEffectiveOrder = dailyEffectiveOrderCountList.stream().reduce(Integer::sum).get();

        // order completion rate = effective order / total order * 100%
        Double orderCompletionRate = 0.0; // possible exception when sumTotalOrder become 0
        orderCompletionRate =
                sumTotalOrder.intValue() == 0 ? 0.0 : ((double) sumEffectiveOrder / sumTotalOrder);

        return OrderReportVO
                .builder()
                .dateList(dateListStr)
                .validOrderCountList(dailyEffectiveOrderCountListStr)
                .orderCountList(dailyTotalOrderCountListStr)
                .validOrderCount(sumEffectiveOrder)
                .totalOrderCount(sumTotalOrder)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * top 10 sales report within date range
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end) {
        // find sales top 10 from orders and order_detail table
        /*
         select od.name, sum(od.number) total
            from (select * from orders where status = 5 and create_time > begin and create_time < end) o
                , order_detail od
            where o.id = od.order_id
            group by od.name
            order by total desc
         */
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList
                = orderMapper.salesTop10(beginTime, endTime, Orders.COMPLETED);

        // Extract name and count elements from map to List
        // JDK8: streams handle collection processing
//        Set<Map.Entry<String, Integer>> entrySet = salesTop10Map.entrySet();
//        List<String> nameList = entrySet.stream().map(s -> s.getKey()).collect(Collectors.toList());
//        List<Integer> numberList = entrySet.stream().map(s -> s.getValue()).collect(Collectors.toList());

        List<String> nameList =
                goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList =
                goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO
                .builder()
                .nameList(listToStrSplitByComma(nameList))
                .numberList(listToStrSplitByComma(numberList))
                .build();
    }

    /**
     * List all records from orders table limit by conditions
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {
        // Page helper assists dynamic interpolation LIMIT query cooperate with XML
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        /*
            select * from orders where number = ? and phone = ? and status = ?
                and order_time > ? and order_time < ?
            limit(0, 10)
            order by order_time desc
         */
        Page<Orders> ordersPage = orderMapper.list(ordersPageQueryDTO);

        return PageResult
                .builder()
                .total(ordersPage.getTotal())
                .records(ordersPage.getResult())
                .build();
    }

    /**
     * Order detail select by order id (order detail, order table in used)
     * @param id
     * @return
     */
    @Override
    public OrderVO detailSelectById(Long id) {
        OrderVO orderVO = new OrderVO(); // order detail, orders table in used

        // select * from orders where id = ?
        Orders order = orderMapper.selectById(id); // settle order table
        BeanUtils.copyProperties(order, orderVO);

        // select * from order_detail where order_id = ?
        List<OrderDetail> orderDetailsList = orderDetailMapper.selectByOrderId(id);
        orderVO.setOrderDetailList(orderDetailsList);

        return orderVO;
    }

    /**
     * update order status in difference scenario
     * @param orderStatusDTO
     */
    public void updateStatus(OrderStatusDTO orderStatusDTO){
        Orders orders = new Orders();
        switch (orderStatusDTO.getOrderStatus()) {
            case CONFIRMED:
                // update status to confirm
                // status = 3(confirm)
                orders = Orders.builder().id(orderStatusDTO.getId())
                        .status(Orders.CONFIRMED).build();
                break;
            case DELIVERY_IN_PROGRESS:
                // update status to delivery in progress
                // status = 3(confirm)
                orders = Orders.builder().id(orderStatusDTO.getId())
                        .status(Orders.DELIVERY_IN_PROGRESS).build();
                break;
            case COMPLETED:
                // update status to delivery in progress
                // status = 3(confirm)
                orders = Orders.builder().id(orderStatusDTO.getId())
                        .status(Orders.COMPLETED).build();
                break;
            case CANCELLED:
                // update status to reject
                // status = 6(cancelled), rejectionReason = ?
                orders = Orders.builder().id(orderStatusDTO.getId()).status(Orders.CANCELLED)
                        .rejectionReason(orderStatusDTO.getRejectionReason()).build();
                break;

        }
        orderMapper.update(orders);
    }



    private List<LocalDate> findAllDateBetween(LocalDate begin, LocalDate end){
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        // find out every date start with begin until end
        while(!begin.equals(end)){
            begin = begin.plusDays(1); // next day
            localDateList.add(begin);
        }
        return localDateList;
    }

    private String listToStrSplitByComma(List<?> list){
        return StringUtils.join(list, ',');
    }
}
