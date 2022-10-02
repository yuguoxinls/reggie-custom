package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-09-30 15:51:00
* @Entity generator.domain.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




