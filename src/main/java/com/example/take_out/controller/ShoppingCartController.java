package com.example.take_out.controller;


import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.ShoppingCart;
import com.example.take_out.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     *
     * @param shoppingCart 购物车信息
     */
    @PostMapping("/add")
    public R<String> addRedis(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.addRedis(shoppingCart);
    }

    /**
     * 减少购物车
     *
     * @param shoppingCart 购物车信息
     * @return 成功
     */
    @PostMapping("/sub")
    public R<String> subRedis(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.subRedis(shoppingCart);
    }

    /**
     * 展示购物车
     *
     * @return 购物车商品列表
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> showRedis() {
        return shoppingCartService.showRedis();
    }

    /**
     * 清空购物车
     *
     * @return 成功
     */
    @DeleteMapping("/clean")
    public R<String> cleanRedis() {
        return shoppingCartService.cleanRedis();
    }
}
