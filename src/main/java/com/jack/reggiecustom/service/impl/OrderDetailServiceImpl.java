package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.mapper.OrderDetailMapper;
import com.jack.reggiecustom.model.domain.OrderDetail;
import com.jack.reggiecustom.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-10-05 16:18:01
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService {

}




