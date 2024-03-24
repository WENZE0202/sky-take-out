package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * insert new employee
     * @param employeeDTO
     */
    void insert(EmployeeDTO employeeDTO);

    /**
     * pagination inquiry
     * @param employeePageQueryDTO
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * employee status update
     * @param status
     * @param id
     */
    Integer startOrFinish(Integer status, Long id);

    /**
     * Employee find by id
     * @param id
     */
    Employee findById(Long id);

    /**
     * Employee detail update
     * @param employeeDTO
     * @return
     */
    Integer updateEmployeeDetail(EmployeeDTO employeeDTO);
}
