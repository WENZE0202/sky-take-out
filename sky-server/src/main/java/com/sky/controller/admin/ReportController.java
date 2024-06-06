package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.UserService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "Order API")
public class ReportController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    /**
     * turnover statistic: sum every entry valid amount within given date time interval
     * @param begin start date
     * @param end end date
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("1. Turnover statistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
            log.info("[STATISTICS] Turnover statistic report date range: {} : {}", begin,end);
            TurnoverReportVO turnoverReportVO = orderService.turnoverStatistics(begin, end);
            return Result.success(turnoverReportVO);
    }

    /**
     * count new user number daily and daily user history total
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("2. User statistics")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("[STATISTICS] User statistic report date range: {} : {}", begin,end);
        UserReportVO userReportVO = userService.userReport(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * daily effective order and total order, count and rate statistics
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("[STATISTICS] Order report day rang: {} - {}", begin, end);
        OrderReportVO orderReportVO = orderService.orderReport(begin, end);
        return Result.success(orderReportVO);
    }
}
