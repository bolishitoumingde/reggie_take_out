package com.example.take_out.service;

import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service
 * @createDate 2022-08-18 12:34:12
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    R<String> addRedis(ShoppingCart shoppingCart);

    R<String> subRedis(ShoppingCart shoppingCart);

    R<List<ShoppingCart>> showRedis();

    R<String> cleanRedis();

}
