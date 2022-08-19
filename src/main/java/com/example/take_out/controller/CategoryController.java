package com.example.take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Category;
import com.example.take_out.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 成功信息
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        // 已存在异常已经被全局异常处理器捕获
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 分类分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return 页面数据
     */
    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") int currentPage,
                                  @RequestParam("pageSize") int pageSize) {
        log.info("当前页：{}，页面大小：{}", currentPage, pageSize);
        Page<Category> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Category::getSort);
        return R.success(categoryService.page(page, lqw));
    }

    /**
     * 删除分类
     *
     * @param id 分类id
     * @return 删除信息
     */
    @DeleteMapping
    public R<String> delById(@RequestParam("ids") Long id) {
        categoryService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 修改信息
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        if (categoryService.updateById(category)) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * 分类分页
     *
     * @param type 分类type
     * @return 分类分页数据
     */
    @GetMapping("/list")
    public R<List<Category>> listCategory(Integer type) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(type != null, Category::getType, type);
        List<Category> categoryList = categoryService.list(lqw);
        return R.success(categoryList);
    }
}
