package com.jack.reggiecustom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.reggiecustom.common.BaseContext;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.model.domain.AddressBook;
import com.jack.reggiecustom.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;



    @GetMapping("/list")
    public BaseResponse list(){
        log.info("get...");
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return ResultUtils.success(list);
    }

    @GetMapping("/default")
    public BaseResponse get() {
        log.info("get...");
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        return ResultUtils.success(addressBook);
    }

    @PostMapping
    public BaseResponse save(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return ResultUtils.success("操作成功！");
    }

    @PutMapping("/default")
    public BaseResponse update(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        for (AddressBook item : list) {
            item.setIsDefault(0);
        }
        addressBookService.updateBatchById(list);

        AddressBook addressBookServiceById = addressBookService.getById(addressBook.getId());
        addressBookServiceById.setIsDefault(1);
        addressBookService.updateById(addressBookServiceById);
        return ResultUtils.success("操作成功！");

    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return ResultUtils.success(addressBook);
    }

    @PutMapping
    public BaseResponse updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return ResultUtils.success("操作成功！");
    }

    @DeleteMapping
    public BaseResponse delete(Long ids){
        addressBookService.removeById(ids);
        return ResultUtils.success("操作成功！");
    }
}
