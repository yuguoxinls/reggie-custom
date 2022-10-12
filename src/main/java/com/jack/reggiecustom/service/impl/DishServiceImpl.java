package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.DishMapper;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.DishFlavor;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.service.CategoryService;
import com.jack.reggiecustom.service.DishFlavorService;
import com.jack.reggiecustom.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    public BaseResponse pageWithCategory(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();

        for (Dish item : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category == null){
                return ResultUtils.error(ErrorCode.NULL_ERROR);
            }
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }

        dishDtoPage.setRecords(list);
        return ResultUtils.success(dishDtoPage);
    }

    @Override
    public BaseResponse updateWithFlavor(DishDto dishDto) {
        //1. 更新菜品基本信息
        this.updateById(dishDto);
        //2. 清除菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //3. 重新保存前端传过来口味信息
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

    @Override
    public BaseResponse listWithFlavors(Dish dish) {
        List<DishDto> dishDtoList; // 最终要返回的对象

        //动态的构造key，因为是按照分类来查询redis中的菜品，分类不同，key也不同
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //根据key从redis中查询
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (dishDtoList != null){
            //不为空说明查到了，也就是redis中存了菜品，直接返回即可
            return ResultUtils.success(dishDtoList);
        }
        //执行到这说明redis中没有，先查数据库，再存到redis
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = this.list(queryWrapper);

        dishDtoList = new ArrayList<>();
        for (Dish item : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            if (dishFlavorList == null){
                return ResultUtils.error(ErrorCode.NULL_ERROR);
            }
            dishDto.setFlavors(dishFlavorList);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            dishDtoList.add(dishDto);
        }
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return ResultUtils.success(dishDtoList);
    }
}




