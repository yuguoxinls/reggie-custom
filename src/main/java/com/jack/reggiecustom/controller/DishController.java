package com.jack.reggiecustom.controller;

import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.dto.DishDto;
import com.jack.reggiecustom.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public BaseResponse add(@RequestBody DishDto dishDto){
        log.info("add....");

        return dishService.saveWithFlavor(dishDto);
    }
}
