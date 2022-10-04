package com.jack.reggiecustom.model.dto;

import com.jack.reggiecustom.model.domain.Dish;
import com.jack.reggiecustom.model.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;
}
