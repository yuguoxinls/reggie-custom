package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.mapper.DishMapper;
import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.service.DishService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-10-03 14:37:42
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService {

}




