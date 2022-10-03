package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-10-03 14:37:42
* @Entity generator.domain.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




