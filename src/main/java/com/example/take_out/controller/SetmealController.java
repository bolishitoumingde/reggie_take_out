package com.example.take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.controller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ISetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") int currentPage,
                                    @RequestParam("pageSize") int pageSize,
                                    String name) {
        log.info("当前页：{}，页面大小：{}，套餐名称：{}", currentPage, pageSize, name);
        return setmealService.getPage(currentPage, pageSize, name);
    }

    /**
     * 添加套餐
     *
     * @param setmealDto 套餐类
     * @return 成功
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.addSetmeal(setmealDto);
    }

    /**
     * 根据套餐id删除
     *
     * @param ids 套餐id
     * @return 成功
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache")
    public R<String> delByIds(@RequestParam("ids") List<Long> ids) {
        return setmealService.removeBatchByIds(ids);
    }

    /**
     * 起售停售
     *
     * @param status 状态
     * @param ids    需要操作的id
     * @return 成功
     */
    @PostMapping("/status/{status}")
    public R<String> stop(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
        return setmealService.stop(status, ids);
    }

    /**
     * 获取套餐分类数据
     *
     * @param setmeal 套餐信息
     * @return 套餐数据
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId, setmeal.getCategoryId()).eq(Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> setmealList = setmealService.list(lqw);
        return R.success(setmealList);
    }

    /**
     * 根据id查询套餐信心
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        return setmealService.getById(id);
    }


}
