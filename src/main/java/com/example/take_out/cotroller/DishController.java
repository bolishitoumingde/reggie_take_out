package com.example.take_out.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.dto.DishDto;
import com.example.take_out.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;


    @GetMapping("/page")
    public R<Page<DishDto>> page(@RequestParam("page") int currentPage,
                                 @RequestParam("pageSize") int pageSize,
                                 String name) {
        log.info("当前页：{}，页面大小：{}，菜品名称：{}", currentPage, pageSize, name);
        return dishService.getPage(currentPage, pageSize, name);
    }

    /**
     * 新增菜品
     *
     * @param dishDto 菜品信息
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        return dishService.addDish(dishDto);
    }

    /**
     * 根据菜品id获取菜品信息
     *
     * @param id 菜品id
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        return dishService.getById(id);
    }

    /**
     * 修改菜品
     *
     * @param dishDto 修改的菜品信息
     */
    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        return dishService.updateDish(dishDto);
    }

    @GetMapping("/list")
    public R<List<Dish>> getDish(Dish dish) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lqw);
        return R.success(list);
    }

    @PostMapping("/status/{status}")
    public R<String> stop(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
        return dishService.stop(status, ids);
    }


}
