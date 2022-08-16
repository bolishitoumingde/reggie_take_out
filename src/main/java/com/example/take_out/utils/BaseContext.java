package com.example.take_out.utils;

/**
 * 基于ThreadLocal封装工具类，实现保存和获取当前登录用户的ID
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setId(Long id) {
        threadLocal.set(id);
    }

    public static Long getId() {
        return threadLocal.get();
    }
}
