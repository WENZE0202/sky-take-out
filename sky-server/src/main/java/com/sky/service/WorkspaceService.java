package com.sky.service;

import com.sky.vo.BusinessDataVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * Business data: turnover, valid order, order completion rate,
     *                       average order price, new users count
     * @return
     */
    BusinessDataVO businessData(LocalDateTime beginTime, LocalDateTime endTime);
}
