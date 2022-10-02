package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Employee;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2022-09-30 15:51:00
*/
public interface EmployeeService extends IService<Employee> {

    public BaseResponse employeeLogin(Employee employee, HttpServletRequest request);

    BaseResponse add(Employee employee, HttpServletRequest request);
}
