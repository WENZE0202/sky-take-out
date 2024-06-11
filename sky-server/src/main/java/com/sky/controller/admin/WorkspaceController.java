package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Workspace API")
public class WorkspaceController {

    /*
    No mapping for GET /admin/workspace/businessData
    No mapping for GET /admin/workspace/overviewOrders
    No mapping for GET /admin/workspace/overviewDishes
    No mapping for GET /admin/workspace/overviewSetmeals
     */
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * Business data(Today): turnover, valid order, order completion rate,
     *                       average order price, new users count
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("1. Business data")
    public Result<BusinessDataVO> businessData(){
        LocalDate now = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(now, LocalTime.MAX);

        log.info("[WORKSPACE] Business data from: {}", now);
        BusinessDataVO businessDataVO = workspaceService.businessData(beginTime, endTime);
        return Result.success(businessDataVO);
    }


}
