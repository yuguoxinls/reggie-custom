package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2022-10-04 10:48:28
* @Entity generator.domain.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




