package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.dto.DishDto;
import com.example.take_out.entity.DishFlavor;
import com.example.take_out.service.IDishFlavorService;
import com.example.take_out.service.IDishService;
import com.example.take_out.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2022-08-17 10:57:25
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private IDishFlavorService dishFlavorService;

    /**
     * 添加新菜品
     *
     * @param dishDto 菜品信息
     * @return 成功
     */
    // TODO 图片信息为空
    @Override
    @Transactional
    public R<String> addDish(DishDto dishDto) {
        // 保存菜品信息
        this.save(dishDto);
        // 获取菜品id
        Long dishDtoId = dishDto.getId();
        // 获取口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 遍历口味，添加菜品id
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        // 保存口味信息
        dishFlavorService.saveBatch(flavors);
        return R.success("添加成功");
    }

    @Override
    public R<Page<Dish>> getPage(int currentPage, int pageSize, String name) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(name), Dish::getName, name);
        lqw.orderByDesc(Dish::getSort);
        Page<Dish> page = new Page<>();
        this.page(page, lqw);
        List<Dish> records = page.getRecords();
        for (Dish record : records) {
            System.out.println(record);
        }
        return R.success(page);
    }
}




