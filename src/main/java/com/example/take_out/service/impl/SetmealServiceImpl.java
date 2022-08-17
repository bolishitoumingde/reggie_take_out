package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.entity.SetmealDish;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import com.example.take_out.mapper.SetmealMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2022-08-17 11:00:29
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements ISetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private ISetmealDishService setmealDishService;

    @Override
    public R<Page<SetmealDto>> getPage(int currentPage, int pageSize, String name) {
        Page<SetmealDto> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<SetmealDto> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(name), Setmeal::getName, name);
        lqw.apply("setmeal.category_id = category.id");
        setmealMapper.getPage(page, lqw);
        return R.success(page);
    }

    @Override
    @Transactional
    public R<String> addSetmeal(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加成功");
    }


    public R<String> removeBatchByIds(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        lqw.eq(Setmeal::getStatus, 1);
        // 存在启售的套餐
        if (this.count(lqw) > 0) {
            return R.error("套餐正在售卖，请先停售");
        }
        this.removeBatchByIds(ids);
        LambdaQueryWrapper<SetmealDish> lqwDish = new LambdaQueryWrapper<>();
        lqwDish.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lqwDish);
        return R.success("删除成功");
    }

    @Override
    public R<String> stop(int status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        this.update(setmeal, lqw);
        return R.success("成功");
    }


}




