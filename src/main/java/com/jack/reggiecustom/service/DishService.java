package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.dto.DishDto;

/**
* @author Administrator
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-10-03 14:37:42
*/
public interface DishService extends IService<Dish> {

    BaseResponse saveWithFlavor(DishDto dishDto);

    BaseResponse pageWithCategory(int page, int pageSize, String name);

    BaseResponse updateWithFlavor(DishDto dishDto);

    BaseResponse listWithFlavors(Dish dish);
}
