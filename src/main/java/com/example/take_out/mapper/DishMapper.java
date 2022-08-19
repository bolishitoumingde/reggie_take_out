package com.example.take_out.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.dto.DishDto;
import com.example.take_out.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author Administrator
 * @description 针对表【dish(菜品管理)】的数据库操作Mapper
 * @createDate 2022-08-17 10:57:25
 * @Entity com.example.take_out.entity.Dish
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    /**
     * 使用SQL联表查询
     *
     * @param page 分页对象
     * @param lqw  查询条件
     * @return 页面对象
     */
    @Select("SELECT dish.*, category.name as categoryName FROM dish,category ${ew.customSqlSegment}")
    Page<DishDto> getPage(Page<DishDto> page, @Param("ew") Wrapper lqw);
}




