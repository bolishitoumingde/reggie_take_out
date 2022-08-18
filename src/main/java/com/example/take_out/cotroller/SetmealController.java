package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.addSetmeal(setmealDto);
    }

    @DeleteMapping
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

    @GetMapping("/list")
    public R<List<Setmeal>> list(@RequestParam("categoryId") Long id, int status) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId, id).eq(Setmeal::getStatus, status);
        List<Setmeal> setmealList = setmealService.list(lqw);
        return R.success(setmealList);
    }


}
