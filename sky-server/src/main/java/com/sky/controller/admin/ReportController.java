package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.TurnoverReportVO;
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
}
