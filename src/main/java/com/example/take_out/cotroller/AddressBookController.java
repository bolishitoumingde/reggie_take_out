package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.AddressBook;
import com.example.take_out.service.IAddressBookService;
import com.example.take_out.utils.BaseContext;
import com.example.take_out.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook 地址类
     * @return 成功
     */
    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook) {
        Long id = (Long) ServletUtil.getSession().getAttribute("user");
        addressBook.setUserId(id);
        addressBookService.save(addressBook);
        return R.success("添加成功");
    }


    /**
     * 修改默认地址
     *
     * @param addressBook 地址类
     * @return 成功
     */
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
        return addressBookService.setDefault(addressBook);
    }

    /**
     * 根据id查询地址
     *
     * @param id id
     * @return 地址信息
     */
    @GetMapping("/{id}")
    public R<Object> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        return R.error("未找到该信息");
    }


    /**
     * 获取默认地址
     *
     * @return 默认地址信息
     */
    @GetMapping("/default")
    public R<Object> getDefault() {
        return addressBookService.getDefault();
    }

    /**
     * 查询指定用户全部地址
     *
     * @param addressBook 地址类
     * @return 该用户全部地址信息
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getId());
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        if (addressBook.getUserId() == null) {
            return R.error("未查询到用户名");
        }
        lqw.eq(AddressBook::getUserId, addressBook.getUserId());
        lqw.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(lqw));
    }

    /**
     * 删除地址
     *
     * @param id 地址id
     * @return 成功失败
     */
    @DeleteMapping
    public R<String> delById(@RequestParam("ids") Long id) {
        if (addressBookService.removeById(id)) {
            return R.success("删除成功");
        }
        return R.error("删除失败");

    }

}
