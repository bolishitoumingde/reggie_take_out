package com.example.take_out.mapper;

import com.example.take_out.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-08-18 09:39:16
* @Entity com.example.take_out.entity.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




