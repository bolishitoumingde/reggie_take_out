package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.entity.AddressBook;
import com.example.take_out.service.AddressBookService;
import com.example.take_out.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-08-18 09:39:16
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




