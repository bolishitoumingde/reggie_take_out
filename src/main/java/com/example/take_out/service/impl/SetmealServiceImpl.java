package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Dish;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.entity.SetmealDish;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import com.example.take_out.mapper.SetmealMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    private ISetmealDishService setmealDishService;

    public R<Page<Setmeal>> getPage(int currentPage, int pageSize, String name) {
        Page<Setmeal> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(name), Setmeal::getName, name);
        this.page(page, lqw);
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
}




