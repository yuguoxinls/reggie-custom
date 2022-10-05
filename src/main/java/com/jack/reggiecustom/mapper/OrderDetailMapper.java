package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-10-05 16:18:01
* @Entity generator.domain.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




