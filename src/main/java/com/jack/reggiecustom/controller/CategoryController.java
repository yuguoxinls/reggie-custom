package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.reggiecustom.common.BaseContext;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public BaseResponse Page(int page, int pageSize, String name){
        log.info("page...");
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Category::getName, name);
        queryWrapper.orderByDesc(Category::getUpdateTime);
        categoryService.page(pageInfo, queryWrapper);
        return ResultUtils.success(pageInfo);
    }

    @PostMapping
    public BaseResponse add(@RequestBody Category category){
        if (category == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        boolean save = categoryService.save(category);
        if (!save){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("操作成功！");
    }

    @PutMapping
    public BaseResponse update(@RequestBody Category category){
        if (category == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        boolean b = categoryService.updateById(category);
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success("操作成功！");
    }

    @DeleteMapping()
    public BaseResponse delete(Long ids){
        log.info("delete...");
        return categoryService.delete(ids);
    }

    @GetMapping("/list")
    public BaseResponse list(int type){
        log.info("list......");
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType, type);
        List<Category> list = categoryService.list(queryWrapper);
        if (list == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(list);
    }
}
