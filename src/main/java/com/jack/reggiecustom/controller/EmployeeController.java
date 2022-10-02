package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.constant.UserConstant;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Employee;
import com.jack.reggiecustom.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public BaseResponse employeeLogin(@RequestBody Employee employee, HttpServletRequest request){
        log.info("login...");
        if (employee == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        return employeeService.employeeLogin(employee, request);
    }

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String name){
        log.info("page...");
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);
        return ResultUtils.success(pageInfo);
    }

    @PostMapping
    public BaseResponse addEmployee(@RequestBody Employee employee, HttpServletRequest request){
        log.info("add...");
        if (employee == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        return employeeService.add(employee, request);//TODO 公共字段自动填充
    }

    @PostMapping("/logout")
    public BaseResponse employeeLogout(HttpServletRequest request){
        if (request == null){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success("退出成功！");
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public BaseResponse update(@RequestBody Employee employee){
        log.info(employee.toString());

        employeeService.updateById(employee);

        return ResultUtils.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     */
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return ResultUtils.success(employee);
        }
        return ResultUtils.error(ErrorCode.NULL_ERROR);
    }

}
