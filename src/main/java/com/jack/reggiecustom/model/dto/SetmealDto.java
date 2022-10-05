package com.jack.reggiecustom.model.dto;

import com.jack.reggiecustom.model.domain.Setmeal;
import com.jack.reggiecustom.model.domain.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
