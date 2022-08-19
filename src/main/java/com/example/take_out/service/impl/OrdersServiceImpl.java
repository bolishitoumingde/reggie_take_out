package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.*;
import com.example.take_out.exception.ServiceException;
import com.example.take_out.service.*;
import com.example.take_out.mapper.OrdersMapper;
import com.example.take_out.utils.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2022-08-18 16:12:41
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements IOrdersService {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private IOrderDetailService orderDetailService;

    /**
     * 提交订单
     *
     * @param orders 订单部分数据
     * @return 成功
     */
    // TODO 订单明细
    @Override
    @Transactional
    public R<String> submitOrder(Orders orders) {
        // 获取用户id
        Long userId = (Long) ServletUtil.getSession().getAttribute("user");
        R<List<ShoppingCart>> res = shoppingCartService.showRedis();
        List<ShoppingCart> shoppingCarts = res.getData();
        // 商品总价
        BigDecimal price = new BigDecimal(0);
        for (ShoppingCart cart : shoppingCarts) {
            price = price.add(cart.getAmount());
        }
        // 购物车为空
        if (CollectionUtils.isEmpty(shoppingCarts))
            throw new ServiceException("购物车为空");
        // 获取用户数据
        User user = userService.getById(userId);
        // 获取地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        // 用户地址为空
        if (addressBook == null)
            throw new ServiceException("用户地址为空");

        // 获取随机id
        long orderId = IdWorker.getId();
        // 设置订单数据
        // 订单号
        orders.setNumber(String.valueOf(orderId));
        // 订单状态：1、待支付  2、待派送
        orders.setStatus(1);
        // 用户id
        orders.setUserId(userId);
        // 地址id
        orders.setAddressBookId(addressBookId);
        // 设置时间
        orders.setOrderTime(LocalDateTime.now());
        // 支付时间
        orders.setCheckoutTime(LocalDateTime.now());
        // 设置价格
        orders.setAmount(price);
        // 手机号
        orders.setPhone(user.getPhone());
        // 地址
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        // 用户名
        orders.setUserName(user.getName());
        // 订单信息保存
        this.save(orders);

        // 订单明细
        // 遍历订单中的商品
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            // 设置菜品或套餐名称
            orderDetail.setName(cart.getName());
            // 设置图片
            orderDetail.setImage(cart.getImage());
            // 设置订单id
            orderDetail.setOrderId(orderId);
            // 设置菜品或套餐id
            if (cart.getDishId() != null) {
                orderDetail.setDishId(cart.getDishId());
                // 设置口味
                orderDetail.setDishFlavor(cart.getDishFlavor());
            } else {
                orderDetail.setSetmealId(cart.getSetmealId());
            }
            // 设置数量
            orderDetail.setNumber(cart.getNumber());
            // 设置价格
            orderDetail.setAmount(cart.getAmount());
            // 保存订单详细信息
            orderDetailService.save(orderDetail);
        }

        // 清空购物车
        shoppingCartService.cleanRedis();
        return R.success("支付成功");
    }

    /**
     * 全部用户订单分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param number      查询订单号
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return 订单页面数据
     */
    @Override
    public R<Page<Orders>> getPage(int currentPage, int pageSize, Long number,
                                   LocalDateTime beginTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        Page<Orders> page = new Page<>();
        // 根据订单号查询
        lqw.eq(number != null, Orders::getNumber, number);
        lqw.ge(beginTime != null, Orders::getOrderTime, beginTime);
        lqw.le(endTime != null, Orders::getOrderTime, endTime);
        this.page(page, lqw);
        return R.success(page);
    }
}




