package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.AddressBook;
import com.example.take_out.service.IAddressBookService;
import com.example.take_out.mapper.AddressBookMapper;
import com.example.take_out.utils.BaseContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @description 针对表【address_book(地址管理)】的数据库操作Service实现
 * @createDate 2022-08-18 09:39:16
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements IAddressBookService {

    @Override
    @Transactional
    public R<String> setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(AddressBook::getUserId, BaseContext.getId());
        lqw.set(AddressBook::getIsDefault, 0);
        this.update(lqw);

        addressBook.setIsDefault(1);
        this.updateById(addressBook);
        return R.success("修改成功");
    }
}




