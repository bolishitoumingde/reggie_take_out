package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.dto.DishDto;
import com.example.take_out.entity.DishFlavor;
import com.example.take_out.service.IDishFlavorService;
import com.example.take_out.service.IDishService;
import com.example.take_out.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2022-08-17 10:57:25
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Resource
    private RedisTemplate<Long, Map<String, DishDto>> redisTemplate;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private IDishFlavorService dishFlavorService;

    /**
     * 后台添加新菜品，需要同步操作redis
     *
     * @param dishDto 新增的菜品包括口味信息
     * @return 成功
     */
    @Override
    @Transactional
    public R<String> addDish(DishDto dishDto) {
        /*
          先添加到数据库
         */
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

        /*
          再缓存到redis
         */
        // 获取分类id
        Long categoryId = dishDto.getCategoryId();
        // redis查询当前分类
        Map<String, DishDto> dtoMap = redisTemplate.opsForValue().get(categoryId);
        if (dtoMap != null) { // 当前菜品分类已存在
            // 添加进map
            dtoMap.put(String.valueOf(dishDto.getId()), dishDto);
            redisTemplate.opsForValue().set(categoryId, dtoMap);
        } else { // 当前不存在于redis
            // 直接调用getDish方法，添加进redis
            this.getDish(dishDto);
        }
        return R.success("添加成功");
    }

    /**
     * 菜品数据分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param searchName  查询条件，菜品名称
     * @return 菜品分页数据
     */
    @Override
    public R<Page<DishDto>> getPage(int currentPage, int pageSize, String searchName) {
        Page<DishDto> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<DishDto> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Dish::getSort);
        lqw.apply(searchName != null, "dish.name like '%" + searchName + "%'");
        lqw.apply("dish.category_id = category.id");
        dishMapper.getPage(page, lqw);
        return R.success(page);
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
        this.updateById(dishDto);
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

    /**
     * 停售起售菜品
     *
     * @param status 状态
     * @param ids    需要修改的菜品ids
     * @return 成功
     */
    @Override
    public R<String> stop(int status, List<Long> ids) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId, ids);
        Dish dish = new Dish();
        dish.setStatus(status);
        this.update(dish, lqw);
        return R.success("成功");
    }

    /**
     * 查询菜品，通过分类id获取所有菜品
     *
     * @param dish 菜品信息，包含分类信息
     * @return 菜品集合
     */
    @Override
    public R<List<DishDto>> getDish(Dish dish) {
        // 根据菜品分类id查询redis数据库
        Map<String, DishDto> map = redisTemplate.opsForValue().get(dish.getCategoryId());
        // 存储菜品包括口味信息
        List<DishDto> dishDtoList = new ArrayList<>();
        if (map == null) { // 当前菜品id未缓存
            // 进行数据库查询
            LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
            lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
            lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
            // 获得当前分类下所有菜品
            List<Dish> dishList = this.list(lqw);
            LambdaQueryWrapper<DishFlavor> lqwDishFlavor;
            // 新map，并放入redis
            Map<String, DishDto> dtoMap = new HashMap<>();
            // 遍历获取口味信息
            for (Dish dish1 : dishList) {
                lqwDishFlavor = new LambdaQueryWrapper<>();
                lqwDishFlavor.eq(DishFlavor::getDishId, dish1.getId());
                // 得到口味列表
                List<DishFlavor> dishFlavorList = dishFlavorService.list(lqwDishFlavor);
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish1, dishDto);
                dishDto.setFlavors(dishFlavorList);
                dishDtoList.add(dishDto);
                dtoMap.put(String.valueOf(dish1.getId()), dishDto);
            }
            redisTemplate.opsForValue().set(dish.getCategoryId(), dtoMap);
        } else {
            // 具体菜品id的键集合
            Set<String> keys = map.keySet();
            // 遍历，添加进List集合
            for (String key : keys) {
                dishDtoList.add(map.get(key));
            }
        }
        return R.success(dishDtoList);
    }
}




