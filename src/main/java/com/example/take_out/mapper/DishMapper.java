package com.example.take_out.mapper;

import com.example.take_out.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author Administrator
 * @description 针对表【dish(菜品管理)】的数据库操作Mapper
 * @createDate 2022-08-17 10:57:25
 * @Entity com.example.take_out.entity.Dish
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}




