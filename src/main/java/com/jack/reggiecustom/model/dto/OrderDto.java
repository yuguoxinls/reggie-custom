package com.jack.reggiecustom.model.dto;

import com.jack.reggiecustom.model.domain.Orders;
import lombok.Data;

@Data
public class OrderDto extends Orders {
    private String userName;
}
