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

import java.util.ArrayList;
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
    public BaseResponse pageWithUser(int page, int pageSize, String number) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(number),Orders::getNumber, number);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        this.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, orderDtoPage, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrderDto> list = new ArrayList<>();

        for (Orders item : records) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            String userName = user.getName();
            orderDto.setUserName(userName);
            list.add(orderDto);
        }
        orderDtoPage.setRecords(list);
        return ResultUtils.success(orderDtoPage);
    }
}




