package com.example.take_out.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Administrator
 * @description 针对表【setmeal(套餐)】的数据库操作Mapper
 * @createDate 2022-08-17 11:00:29
 * @Entity com.example.take_out.entity.Setmeal
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    @Select("SELECT setmeal.*, category.name as categoryName FROM setmeal,category ${ew.customSqlSegment}")
    Page<SetmealDto> getPage(@Param("page") Page<SetmealDto> page, @Param("ew") Wrapper lqw);

}




