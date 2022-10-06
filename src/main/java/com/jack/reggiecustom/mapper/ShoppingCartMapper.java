package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-10-06 16:49:13
* @Entity generator.domain.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




