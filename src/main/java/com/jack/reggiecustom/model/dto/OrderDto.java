package com.jack.reggiecustom.model.dto;

import com.jack.reggiecustom.model.domain.OrderDetail;
import com.jack.reggiecustom.model.domain.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

//    private String userName;

    private List<OrderDetail> orderDetails;
}
