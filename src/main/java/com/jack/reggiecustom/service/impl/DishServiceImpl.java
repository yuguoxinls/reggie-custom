package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.DishMapper;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.DishFlavor;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.service.DishFlavorService;
import com.jack.reggiecustom.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-10-03 14:37:42
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public BaseResponse saveWithFlavor(DishDto dishDto) {
        //1. 保存菜品基本信息
        this.save(dishDto);
        //2. 保存菜品口味信息
        //2.1 获取当前菜品的id
        Long id = dishDto.getId();
        //2.2 根据菜品id向数据库中插入数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        boolean b = dishFlavorService.saveBatch(flavors);
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("操作成功");
    }
}




