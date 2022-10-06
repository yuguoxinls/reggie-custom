package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Orders;

import java.util.Date;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-10-05 16:17:55
*/
public interface OrdersService extends IService<Orders> {


    BaseResponse pageWithUser(int page, int pageSize, String number, String beginTime, String endTime);
}
