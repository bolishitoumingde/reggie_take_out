package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Setmeal;
import com.example.take_out.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;

    @GetMapping("/page")
    public R<Page<Setmeal>> page(@RequestParam("page") int currentPage,
                                 @RequestParam("pageSize") int pageSize,
                                 String name) {
        log.info("当前页：{}，页面大小：{}，套餐名称：{}", currentPage, pageSize, name);
        return setmealService.getPage(currentPage, pageSize, name);

    }
}
