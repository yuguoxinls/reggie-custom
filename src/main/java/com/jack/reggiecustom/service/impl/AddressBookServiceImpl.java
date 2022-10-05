package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.mapper.AddressBookMapper;
import com.jack.reggiecustom.model.domain.AddressBook;
import com.jack.reggiecustom.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-10-05 16:23:34
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService {

}




