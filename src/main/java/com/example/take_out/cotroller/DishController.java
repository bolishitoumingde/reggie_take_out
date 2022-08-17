package com.example.take_out.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    @GetMapping("/page")
    public R<Page<Dish>> page(@RequestParam("page") int currentPage,
                              @RequestParam("pageSize") int pageSize) {
        log.info("当前页：{}，页面大小：{}", currentPage, pageSize);
        Page<Dish> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Dish::getSort);
        return R.success(dishService.page(page, lqw));
    }

}
