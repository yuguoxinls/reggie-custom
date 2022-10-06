package com.jack.reggiecustom.controller;

import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.Orders;
import com.jack.reggiecustom.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;

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
}
