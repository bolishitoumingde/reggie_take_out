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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public R<Page<DishDto>> getPage(int currentPage, int pageSize, String name) {
        Page<DishDto> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<DishDto> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(name), Dish::getName, name);
        lqw.orderByDesc(Dish::getSort);
        lqw.apply("dish.category_id = category.id");
        dishMapper.getPage(page, lqw);
        return R.success(page);
        // LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        // lqw.like(Strings.isNotEmpty(name), Dish::getName, name);
        // lqw.orderByDesc(Dish::getSort);
        // Page<Dish> page = new Page<>(currentPage, pageSize);
        // this.page(page, lqw);
        // log.info(page.getRecords().toString());
        // return R.success(page);
    }

    /**
     * 修改菜品信息时，先获取菜品信息
     *
     * @param id 菜品id
     * @return 成功
     */
    @Override
    public R<DishDto> getById(Long id) {
        // 查询菜品信息
        Dish dish = dishMapper.selectById(id);
        // 创建DishDto对象
        DishDto dishDto = new DishDto();
        // 属性拷贝
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        // 根据菜品id获取口味列表
        lqw.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavorList = dishFlavorService.list(lqw);
        // 设置口味列表
        dishDto.setFlavors(flavorList);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto 修改的菜品信息
     */
    @Override
    public R<String> updateDish(DishDto dishDto) {
        // 更新dish表
        this.updateDish(dishDto);
        // 获取菜品id
        Long dishDtoId = dishDto.getId();
        // 清理当前菜品的口味信息
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, dishDtoId);
        // 已经设置逻辑删除
        dishFlavorService.remove(lqw);
        // 获取口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 遍历口味，添加菜品id
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        // 保存口味信息
        dishFlavorService.saveBatch(flavors);
        return R.success("修改成功");
    }

    @Override
    public R<String> stop(int status, List<Long> ids) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId, ids);
        Dish dish = new Dish();
        dish.setStatus(status);
        this.update(dish, lqw);
        return R.success("成功");
    }

    @Override
    public R<List<DishDto>> getDish(Dish dish) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = this.list(lqw);
        List<DishDto> dishDtos = new ArrayList<>();
        LambdaQueryWrapper<DishFlavor> lqwDishFlavor;
        for (Dish dish1 : list) {
            lqwDishFlavor = new LambdaQueryWrapper<>();
            lqwDishFlavor.eq(DishFlavor::getDishId, dish1.getId());
            List<DishFlavor> list1 = dishFlavorService.list(lqwDishFlavor);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);
            dishDto.setFlavors(list1);
            dishDtos.add(dishDto);
        }
        for (DishDto dto : dishDtos) {
            log.info(dto.toString());
        }
        return R.success(dishDtos);
    }
}




