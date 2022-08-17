package com.example.take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Administrator
 * @description 针对表【setmeal(套餐)】的数据库操作Service
 * @createDate 2022-08-17 11:00:29
 */
public interface ISetmealService extends IService<Setmeal> {

    R<Page<Setmeal>> getPage(int currentPage, int pageSize, String name);

}
