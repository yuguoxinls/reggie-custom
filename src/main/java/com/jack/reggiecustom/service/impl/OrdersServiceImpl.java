package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.OrdersMapper;
import com.jack.reggiecustom.model.domain.Orders;
import com.jack.reggiecustom.model.domain.User;
import com.jack.reggiecustom.model.dto.OrderDto;
import com.jack.reggiecustom.service.OrdersService;
import com.jack.reggiecustom.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-10-05 16:17:55
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService {

    @Autowired
    private UserService userService;

    @Override
    public BaseResponse pageWithUser(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(number),Orders::getNumber, number);

        Date begin = null;
        Date end = null;
        if (!StringUtils.isAnyBlank(beginTime, endTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                begin = sdf.parse(beginTime);
                end = sdf.parse(endTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            queryWrapper.between(Orders::getOrderTime, begin, end);
        }

        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        this.page(pageInfo, queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        for (Orders record : records) {
            Long userId = record.getUserId();
            User user = userService.getById(userId);
            String userName = user.getName();
            record.setUserName(userName);
        }

        return ResultUtils.success(pageInfo);
    }
}




