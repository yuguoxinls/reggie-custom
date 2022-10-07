package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.ShoppingCart;

/**
* @author Administrator
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2022-10-06 16:49:13
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    BaseResponse add(ShoppingCart shoppingCart);
}
