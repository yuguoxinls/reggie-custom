package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2022-10-05 16:17:55
* @Entity generator.domain.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




