package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.DishFlavor;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.service.CategoryService;
import com.jack.reggiecustom.service.DishFlavorService;
import com.jack.reggiecustom.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public BaseResponse add(@RequestBody DishDto dishDto){
        log.info("add....");

        return dishService.saveWithFlavor(dishDto);
    }

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String name){
        log.info("page...");

        return dishService.pageWithCategory(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id){
        log.info("getById...");
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);

        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        if (flavors == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        dishDto.setFlavors(flavors);
        return ResultUtils.success(dishDto);
    }

    @PutMapping
    public BaseResponse save(@RequestBody DishDto dishDto){
        return dishService.updateWithFlavor(dishDto);
    }

    @DeleteMapping
    public BaseResponse delete(@RequestParam List<Long> ids){
        boolean b = dishService.removeByIds(ids);
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public BaseResponse status(@PathVariable int status, @RequestParam List<Long> ids){
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            boolean b = dishService.updateById(dish);
            if (!b){
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
            }
        }
        return ResultUtils.success("操作成功！");
    }

    @GetMapping("/list")
    public BaseResponse list(Dish dish){
        log.info("categoryId......");

        return dishService.listWithFlavors(dish);
    }
}
