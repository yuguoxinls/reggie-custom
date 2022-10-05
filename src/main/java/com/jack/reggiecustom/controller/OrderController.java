package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.Orders;
import com.jack.reggiecustom.model.dto.OrderDto;
import com.jack.reggiecustom.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String number){

        return ordersService.pageWithUser(page, pageSize, number); //TODO 分页查询用户信息显示不出来
    }
}
