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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
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
    public BaseResponse list(Setmeal setmeal){
        if (setmeal == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);

        if (list == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(list);
    }
}
