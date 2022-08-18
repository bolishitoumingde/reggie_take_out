package com.example.take_out.mapper;

import com.example.take_out.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-08-18 12:34:12
* @Entity com.example.take_out.entity.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




