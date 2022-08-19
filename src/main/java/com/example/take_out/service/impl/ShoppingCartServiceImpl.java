package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.ShoppingCart;
import com.example.take_out.service.IShoppingCartService;
import com.example.take_out.mapper.ShoppingCartMapper;
import com.example.take_out.utils.ServletUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Administrator
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2022-08-18 12:34:12
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements IShoppingCartService {

    // @Autowired
    @Resource
    private RedisTemplate<Long, Map<String, ShoppingCart>> redisTemplate;

    /**
     * 添加购物车
     *
     * @param shoppingCart 购物车类
     * @return 成功
     */
    @Override
    public R<String> addRedis(ShoppingCart shoppingCart) {
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        Map<String, ShoppingCart> map = redisTemplate.opsForValue().get(userId);
        // 获取菜品或套餐id
        String key = String.valueOf(shoppingCart.getDishId() != null ?
                shoppingCart.getDishId() : shoppingCart.getSetmealId());
        // 第一次添加购物车
        if (map == null) {
            map = new HashMap<>();
            shoppingCart.setNumber(1);
            map.put(key, shoppingCart);
            redisTemplate.opsForValue().set(userId, map);
            return R.success("添加成功");
        }
        // 当前key已存在
        if (map.containsKey(key)) {
            ShoppingCart cart = map.get(key);
            // 修改数量
            cart.setNumber(cart.getNumber() + 1);
            redisTemplate.opsForValue().set(userId, map);
        } else { // key不存在
            shoppingCart.setNumber(1);
            map.put(key, shoppingCart);
            redisTemplate.opsForValue().set(userId, map);
        }
        return R.success("添加成功");
    }

    /**
     * 减少购物车
     *
     * @param shoppingCart 购物车信息
     * @return 成功
     */
    @Override
    public R<String> subRedis(ShoppingCart shoppingCart) {
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        Map<String, ShoppingCart> map = redisTemplate.opsForValue().get(userId);
        // 获取菜品或套餐id
        String key = String.valueOf(shoppingCart.getDishId() != null ?
                shoppingCart.getDishId() : shoppingCart.getSetmealId());
        if (map == null) return R.error("购物车为空");

        ShoppingCart cart = map.get(key);
        // 当前菜品key不存在
        if (cart == null)
            return R.error("当前菜品不存在");

        // 已经只有一个，删除该菜品
        if (cart.getNumber() == 1) {
            map.remove(key);
        } else {
            cart.setNumber(cart.getNumber() - 1);
        }
        redisTemplate.opsForValue().set(userId, map);
        return R.success("成功");
    }

    /**
     * 展示购物车
     *
     * @return 购物车集合
     */
    @Override
    public R<List<ShoppingCart>> showRedis() {
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        Map<String, ShoppingCart> map = redisTemplate.opsForValue().get(userId);
        if (map == null) {
            return null;
        }
        Collection<ShoppingCart> values = map.values();
        List<ShoppingCart> shoppingCarts = values.stream().toList();
        return R.success(shoppingCarts);
    }

    /**
     * 清空购物车
     *
     * @return 成功
     */
    @Override
    public R<String> cleanRedis() {
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        redisTemplate.delete(userId);
        return R.success("清空成功");
    }


}




