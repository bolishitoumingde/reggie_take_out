package com.example.take_out.cotroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.dto.DishDto;
import com.example.take_out.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;


    @GetMapping("/page")
    public R<Page<Dish>> page(@RequestParam("page") int currentPage,
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

}
