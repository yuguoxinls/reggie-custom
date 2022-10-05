package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.Setmeal;
import com.jack.reggiecustom.model.dto.SetmealDto;

/**
* @author Administrator
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-10-03 14:37:51
*/
public interface SetmealService extends IService<Setmeal> {

    BaseResponse saveWithDish(SetmealDto setmealDto);

    BaseResponse pageWithCategory(int page, int pageSize, String name);

    BaseResponse updateWithDish(SetmealDto setmealDto);
}
