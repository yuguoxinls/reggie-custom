package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-10-02 15:09:51
* @Entity generator.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




