package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.AddressBook;
import com.example.take_out.service.IAddressBookService;
import com.example.take_out.mapper.AddressBookMapper;
import com.example.take_out.utils.ServletUtil;
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

    /**
     * 设置默认地址
     *
     * @param addressBook 地址表信息
     * @return 成功
     */
    @Override
    @Transactional
    public R<String> setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(AddressBook::getUserId, ServletUtil.getSession().getAttribute("user"));
        lqw.set(AddressBook::getIsDefault, 0);
        this.update(lqw);

        addressBook.setIsDefault(1);
        this.updateById(addressBook);
        return R.success("修改成功");
    }

    /**
     * 获取默认地址
     *
     * @return 地址或失败信息
     */
    @Override
    public R<Object> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, ServletUtil.getSession().getAttribute("user"));
        lqw.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = this.getOne(lqw);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        return R.error("未找到默认地址");
    }
}




