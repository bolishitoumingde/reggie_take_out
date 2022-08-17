package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.entity.Category;
import com.example.take_out.mapper.CategoryMapper;
import com.example.take_out.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2022-08-17 10:06:53
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
}
