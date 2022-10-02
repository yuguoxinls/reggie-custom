package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.mapper.CategoryMapper;
import com.jack.reggiecustom.model.domain.Category;
import com.jack.reggiecustom.service.CategoryService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-10-02 15:09:51
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {


}




