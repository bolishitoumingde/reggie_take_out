package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Category;
import com.example.take_out.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


}
