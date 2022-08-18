package com.example.take_out.mapper;

import com.example.take_out.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2022-08-18 16:12:41
 * @Entity com.example.take_out.entity.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




