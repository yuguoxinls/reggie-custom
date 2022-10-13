package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.DishFlavor;
import com.jack.reggiecustom.model.domain.Setmeal;
import com.jack.reggiecustom.model.domain.SetmealDish;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.model.dto.SetmealDto;
import com.jack.reggiecustom.service.SetmealDishService;
import com.jack.reggiecustom.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public BaseResponse save(@RequestBody SetmealDto setmealDto){
        log.info("save...");
        return setmealService.saveWithDish(setmealDto);
    }

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String name){
        log.info("page...");

        return setmealService.pageWithCategory(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id){
        log.info("getById...");
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        return ResultUtils.success(setmealDto);

    }

    @PutMapping
    public BaseResponse update(@RequestBody SetmealDto setmealDto){
        return setmealService.updateWithDish(setmealDto);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public BaseResponse delete(@RequestParam List<Long> ids){
        for (Long setmealId : ids) {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
            setmealDishService.remove(queryWrapper);
        }
        boolean b = setmealService.removeByIds(ids);
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("操作成功！");
    }

    @PostMapping("/status/{status}")
    public BaseResponse status(@RequestParam List<Long> ids, @PathVariable int status){
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            boolean b = setmealService.updateById(setmeal);
            if (!b){
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
            }
        }
        return ResultUtils.success("操作成功！");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public BaseResponse list(Setmeal setmeal){
        if (setmeal == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        List<Setmeal> list;
        /*//动态的构造key，因为是按照分类来查询redis中的菜品，分类不同，key也不同
        String key = "setmeal_" + setmeal.getCategoryId() + "_" + setmeal.getStatus();
        //根据key从redis中查询
        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);

        if (list != null){
            //不为空说明查到了，也就是redis中存了菜品，直接返回即可
            return ResultUtils.success(list);
        }*/

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        list = setmealService.list(queryWrapper);

        if (list == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
//        redisTemplate.opsForValue().set(key, list, 60, TimeUnit.MINUTES);
        return ResultUtils.success(list);
    }
}
