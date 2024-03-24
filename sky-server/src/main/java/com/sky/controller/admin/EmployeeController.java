package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Employee Related Api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "Employee Login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Employee Logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * insert new employee
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Employee Insert")
    public Result insert(@RequestBody EmployeeDTO employeeDTO){
        log.info("employee insert: {}", employeeDTO);
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    /**
     * inquiry pagination
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Employee Pagination")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("pagination inquiry: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * employee update status
     * @param status
     * @param id
     * @return
     */
    @PostMapping ("/status/{status}")
    @ApiOperation(value = "Employee Update Status")
    public Result enableOrDisable(@PathVariable Integer status, Long id){
        // update specific id employee status
        Integer count = employeeService.startOrFinish(status, id);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }

    /**
     * Employee find by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Employee find by id")
    public Result findById(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        return Result.success(employee);
    }

    /**
     * Employee detail update
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Employee detail update")
    public Result updateEmployeeDetail(@RequestBody EmployeeDTO employeeDTO){
        Integer count = employeeService.updateEmployeeDetail(employeeDTO);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }

}
