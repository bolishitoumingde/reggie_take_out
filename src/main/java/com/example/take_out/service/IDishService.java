package com.example.take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.take_out.dto.DishDto;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2022-08-17 10:57:25
 */
public interface IDishService extends IService<Dish> {

    R<String> addDish(DishDto dishDto);

    R<Page<DishDto>> getPage(int currentPage, int pageSize, String name);

    R<DishDto> getById(Long id);

    R<String> updateDish(DishDto dishDto);

    R<String> stop(int status, List<Long> ids);

    R<List<DishDto>> getDish(Dish dish);
}
