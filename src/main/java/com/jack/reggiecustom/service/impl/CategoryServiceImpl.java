package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.CategoryMapper;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.Setmeal;
import com.jack.reggiecustom.service.CategoryService;
import com.jack.reggiecustom.service.DishService;
import com.jack.reggiecustom.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-10-02 15:09:51
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public BaseResponse delete(Long ids) {
        if (ids == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, ids);
        int dishCount = dishService.count(queryWrapper);
        if (dishCount > 0){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "无法删除分类，当前分类关联了菜品！");
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int setmealCount = setmealService.count(lambdaQueryWrapper);
        if (setmealCount > 0){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "无法删除分类，当前分类关联了套餐！");
        }
        this.removeById(ids);
        return ResultUtils.success("删除分类成功！");
    }
}




