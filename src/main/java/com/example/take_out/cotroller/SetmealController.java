package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.dto.SetmealDto;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.service.ISetmealDishService;
import com.example.take_out.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ISetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page<Setmeal>> page(@RequestParam("page") int currentPage,
                                 @RequestParam("pageSize") int pageSize,
                                 String name) {
        log.info("当前页：{}，页面大小：{}，套餐名称：{}", currentPage, pageSize, name);
        return setmealService.getPage(currentPage, pageSize, name);

    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.addSetmeal(setmealDto);
    }
}
