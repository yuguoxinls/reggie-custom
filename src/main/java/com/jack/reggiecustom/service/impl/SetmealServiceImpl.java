package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.SetmealMapper;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.Setmeal;
import com.jack.reggiecustom.model.domain.SetmealDish;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.model.dto.SetmealDto;
import com.jack.reggiecustom.service.CategoryService;
import com.jack.reggiecustom.service.SetmealDishService;
import com.jack.reggiecustom.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-10-03 14:37:51
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public BaseResponse saveWithDish(SetmealDto setmealDto) {
        //1. 保存套餐基本信息
        this.save(setmealDto);
        //2. 保存套餐包含菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId().toString());
        }
        setmealDishService.saveBatch(setmealDishes);
       /* //清理菜品缓存数据，两种方式
        //方式一：清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        //方式二：清理某个分类下的菜品缓存
        String key = "setmeal_" + setmealDto.getCategoryId() + "_" + setmealDto.getStatus();
        redisTemplate.delete(key);*/
        return ResultUtils.success("操作成功！");
    }

    @Override
    public BaseResponse pageWithCategory(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        this.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = new ArrayList<>();

        for (Setmeal item : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category == null){
                return ResultUtils.error(ErrorCode.NULL_ERROR);
            }
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            list.add(setmealDto);
        }

        setmealDtoPage.setRecords(list);
        return ResultUtils.success(setmealDtoPage);
    }

    @Override
    @CacheEvict(value = "setmealCache", allEntries = true)
    public BaseResponse updateWithDish(SetmealDto setmealDto) {
        //1. 更新套餐基本信息
        this.updateById(setmealDto);
        //2. 更新套餐包含菜品信息
        //2.1 删除原始信息
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);
        //2.2 保存新的信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId.toString());
        }
        setmealDishService.saveBatch(setmealDishes);
       /* //清理菜品缓存数据，两种方式
        //方式一：清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        //方式二：清理某个分类下的菜品缓存
        String key = "setmeal_" + setmealDto.getCategoryId() + "_" + setmealDto.getStatus();
        redisTemplate.delete(key);*/

        return ResultUtils.success("操作成功！");
    }
}




