package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Category;

/**
* @author Administrator
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-10-02 15:09:51
*/
public interface CategoryService extends IService<Category> {

    public BaseResponse delete(Long ids);
}
