package com.example.take_out.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Dish;
import com.example.take_out.dto.DishDto;
import com.example.take_out.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    /**
     * 菜品分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param name        查询条件 菜品名称
     * @return 菜品分页数据
     */
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

    /**
     * 获取菜品信息，包括口味信息
     *
     * @param dish 菜品id
     * @return 菜品信息DishDto
     */
    @GetMapping("/list")
    public R<List<DishDto>> getDish(Dish dish) {
        return dishService.getDish(dish);
    }

    /**
     * 起售停售
     *
     * @param status 状态
     * @param ids    需要修改状态的ids
     * @return 成功
     */
    @PostMapping("/status/{status}")
    public R<String> stop(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
        return dishService.stop(status, ids);
    }

    /**
     * 根据id删除菜品
     *
     * @param ids 菜品id集合
     * @return 成功
     */
    @DeleteMapping
    public R<String> delById(@RequestParam("ids") List<Long> ids) {
        dishService.removeBatchByIds(ids);
        return R.success("删除成功");
    }
}
