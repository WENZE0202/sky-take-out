package com.sky.service;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {
    /**
     * export recent 30 days report
     * @param httpServletResponse
     */
    void export(HttpServletResponse httpServletResponse);
}
