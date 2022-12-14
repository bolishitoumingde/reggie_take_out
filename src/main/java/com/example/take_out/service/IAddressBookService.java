package com.example.take_out.service;

import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Administrator
 * @description 针对表【address_book(地址管理)】的数据库操作Service
 * @createDate 2022-08-18 09:39:16
 */
public interface IAddressBookService extends IService<AddressBook> {

    R<String> setDefault(AddressBook addressBook);

    R<Object> getDefault();

}
