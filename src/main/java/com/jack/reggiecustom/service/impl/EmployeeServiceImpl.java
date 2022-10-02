package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.constant.UserConstant;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.mapper.EmployeeMapper;
import com.jack.reggiecustom.model.domain.Employee;
import com.jack.reggiecustom.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-09-30 15:51:00
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService {

    @Override
    public BaseResponse employeeLogin(Employee employee, HttpServletRequest request) {
        if (employee == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        String username = employee.getUsername();
        String password = employee.getPassword();
        if (StringUtils.isAnyBlank(username, password)){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee user = this.getOne(queryWrapper);
        if (user == null){
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        String userPassword = user.getPassword();
        if (!userPassword.equals(password)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        Integer status = user.getStatus();
        if (status == 0){
            return ResultUtils.error(ErrorCode.NO_AUTH, "员工已禁用");
        }

        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user.getId());
        return ResultUtils.success("登录成功！");
    }

    @Override
    public BaseResponse add(Employee employee, HttpServletRequest request) {
        if (employee == null || request == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        String initPassword = "123456";
        initPassword = DigestUtils.md5DigestAsHex(initPassword.getBytes());


        employee.setPassword(initPassword);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        boolean flag = this.save(employee);

        return ResultUtils.success(flag);
    }
}




