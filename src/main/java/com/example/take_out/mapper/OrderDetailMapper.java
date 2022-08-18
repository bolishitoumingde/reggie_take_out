package com.example.take_out.mapper;

import com.example.take_out.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-08-18 16:15:04
* @Entity com.example.take_out.entity.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




