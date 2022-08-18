package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Orders;
import com.example.take_out.service.IOrdersService;
import com.example.take_out.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrdersService ordersService;

    /**
     * 用户支付下单
     *
     * @return 成功
     */
    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody Orders orders) {
        return ordersService.submitOrder(orders);
    }

    /**
     * 用户订单分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return 页面数据
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> userPage(@RequestParam("page") int currentPage, int pageSize) {
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        Page<Orders> page = new Page<>(currentPage, pageSize);
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        lqw.eq(Orders::getUserId, userId);
        ordersService.page(page, lqw);
        return R.success(page);
    }

    /**
     * 全部订单分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param number      查询id
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return 订单页面分页数据
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam("page") int currentPage, int pageSize, Long number,
                                LocalDateTime beginTime, LocalDateTime endTime) {
        return ordersService.getPage(currentPage, pageSize, number, beginTime, endTime);
    }
}
