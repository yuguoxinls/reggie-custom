package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseContext;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.ShoppingCartMapper;
import com.jack.reggiecustom.model.domain.ShoppingCart;
import com.jack.reggiecustom.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-10-06 16:49:13
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService {

    @Override
    public BaseResponse add(ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();

        ShoppingCart cartServiceOne = new ShoppingCart();
        addShoppingCart(dishId, cartServiceOne, this, shoppingCart, userId);
//        if (dishId == null){
//            addShoppingCart(dishId, cartServiceOne, this, shoppingCart);
//        }else {
//            addShoppingCart(dishId, cartServiceOne, this, shoppingCart);
//        }
        return ResultUtils.success(cartServiceOne);
    }

    @Override
    public BaseResponse sub(ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();

        ShoppingCart cartServiceOne = new ShoppingCart();
        subShoppingCart(dishId, cartServiceOne, this, shoppingCart, userId);
        return ResultUtils.success(cartServiceOne);
    }

    private void addShoppingCart(Long id, ShoppingCart cartServiceOne, ShoppingCartService shoppingCartService, ShoppingCart shoppingCart, Long userId){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (id == null){
            id = shoppingCart.getSetmealId();
            queryWrapper.eq(ShoppingCart::getSetmealId, id);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId, id);
        }

        cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne == null){
            //购物车中没有
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }else {
            //购物车中已经有了
            cartServiceOne.setNumber(cartServiceOne.getNumber() + 1);
            shoppingCartService.updateById(cartServiceOne);
        }
    }

    private void subShoppingCart(Long id, ShoppingCart cartServiceOne, ShoppingCartService shoppingCartService, ShoppingCart shoppingCart, Long userId){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (id == null){
            id = shoppingCart.getSetmealId();
            queryWrapper.eq(ShoppingCart::getSetmealId, id);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId, id);
        }

        cartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer number = cartServiceOne.getNumber();
        assert number >= 1;
        if (number > 1){
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCartService.removeById(cartServiceOne.getId());
        }
    }
}




