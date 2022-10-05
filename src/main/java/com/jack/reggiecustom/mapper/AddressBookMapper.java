package com.jack.reggiecustom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jack.reggiecustom.model.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-10-05 16:23:34
* @Entity generator.domain.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




