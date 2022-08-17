package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.entity.Category;
import com.example.take_out.entity.Dish;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.exception.ServiceException;
import com.example.take_out.mapper.CategoryMapper;
import com.example.take_out.service.ICategoryService;
import com.example.take_out.service.IDishService;
import com.example.take_out.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author Administrator
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2022-08-17 10:06:53
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private IDishService dishService;

    @Autowired
    private ISetmealService setmealService;

    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        Category category = categoryMapper.selectById(id);
        // 得到当前分类的type 1-菜品 2-套餐
        Integer type = category.getType();
        long count;
        if (type == 1) {
            LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
            dishLqw.eq(Dish::getCategoryId, id);
            count = dishService.count(dishLqw);
        } else {
            LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<>();
            setmealLqw.eq(Setmeal::getCategoryId, id);
            count = setmealService.count(setmealLqw);
        }
        if (count > 0) {
            throw new ServiceException("当前分类下存在菜品或套餐，无法删除");
        }
        return super.removeById(id);
    }
}
