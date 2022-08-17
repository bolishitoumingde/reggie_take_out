package com.example.take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.take_out.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
 * @createDate 2022-08-17 10:06:53
 * @Entity com.example.take_out.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {


}
