package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggiecustom.common.BaseContext;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.OrderDetail;
import com.jack.reggiecustom.model.domain.Orders;
import com.jack.reggiecustom.model.domain.ShoppingCart;
import com.jack.reggiecustom.model.dto.OrderDto;
import com.jack.reggiecustom.service.OrderDetailService;
import com.jack.reggiecustom.service.OrdersService;
import com.jack.reggiecustom.service.ShoppingCartService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String number, String beginTime, String endTime){

        return ordersService.pageWithUser(page, pageSize, number, beginTime, endTime);
    }

    @PutMapping
    public BaseResponse status(@RequestBody Orders orders){
        boolean b = ordersService.updateById(orders);
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("操作成功！");
    }

    @PostMapping("/submit")
    public BaseResponse submit(@RequestBody Orders orders){
        return ordersService.submit(orders);
    }

    @GetMapping("/userPage")
    public BaseResponse Page(int page, int pageSize){
        return ordersService.pageWithOrderDetail(page, pageSize);
    }

    @PostMapping("/again")
    public BaseResponse again(@RequestBody Orders orders){

        return ordersService.again(orders);
    }


}
