package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseContext;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.OrdersMapper;
import com.jack.reggiecustom.model.domain.*;
import com.jack.reggiecustom.model.dto.OrderDto;
import com.jack.reggiecustom.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

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

    @Override
    public BaseResponse submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        long orderId = IdWorker.getId();// 订单号
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (ShoppingCart item : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            orderDetails.add(orderDetail);
        }

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        User user = userService.getById(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        this.save(orders);
        orderDetailService.saveBatch(orderDetails);
        shoppingCartService.remove(queryWrapper);
        return ResultUtils.success("下单成功！");
    }

    @Override
    public BaseResponse pageWithOrderDetail(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> dtoPage = new Page<>();

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        List<Orders> orders = this.list(queryWrapper);

        List<OrderDto> orderDtos = new ArrayList<>();
        for (Orders order : orders) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);

            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
            orderDto.setOrderDetails(orderDetails);
            orderDtos.add(orderDto);
        }
        dtoPage.setRecords(orderDtos);
        return ResultUtils.success(dtoPage);
    }

    @Override
    public BaseResponse again(Orders orders) {
        Long orderId = orders.getId();
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);


        Long userId = BaseContext.getCurrentId();
        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setName(orderDetail.getName());
            shoppingCart.setImage(orderDetail.getImage());
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(orderDetail.getDishId());
            shoppingCart.setSetmealId(orderDetail.getSetmealId());
            shoppingCart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }
        return ResultUtils.success("订单信息再现成功！");
    }
}




