package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.controller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.entity.SetmealDish;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import com.example.take_out.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 套餐数据分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param searchName  查询条件，套餐名称
     * @return 套餐分页数据
     */
    @Override
    public R<Page<SetmealDto>> getPage(int currentPage, int pageSize, String searchName) {
        Page<SetmealDto> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<SetmealDto> lqw = new LambdaQueryWrapper<>();
        lqw.apply(searchName != null, "setmeal.name like '%" + searchName + "%'");
        lqw.apply("setmeal.category_id = category.id");
        setmealMapper.getPage(page, lqw);
        return R.success(page);
    }

    /**
     * 新建套餐
     *
     * @param setmealDto 套餐信息
     * @return 成功
     */
    @Override
    @Transactional
    public R<String> addSetmeal(SetmealDto setmealDto) {
        /*
         * 保存到数据库
         */
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加成功");
    }


    /**
     * 删除套餐，删除redis相关数据
     *
     * @param ids 套餐id
     * @return 成功
     */
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

    /**
     * 停售起售
     *
     * @param status 状态
     * @param ids    需要操作的ids
     * @return 成功
     */
    @Override
    public R<String> stop(int status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        this.update(setmeal, lqw);
        return R.success("成功");
    }

    /**
     * 查询套餐信息
     *
     * @param id 套餐id
     * @return 套餐信息
     */
    @Override
    public R<SetmealDto> getById(Long id) {
        // 查询套餐信息
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishList = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(dishList);
        return R.success(setmealDto);
    }


}




