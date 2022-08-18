package com.example.take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * @author Administrator
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2022-08-18 16:12:41
 */
public interface IOrdersService extends IService<Orders> {

    R<String> submitOrder(Orders orders);

    R<Page<Orders>> getPage(int currentPage, int pageSize, Long number,
                            LocalDateTime beginTime, LocalDateTime endTime);

}
