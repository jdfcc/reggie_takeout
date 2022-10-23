package com.jdfcc.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户id
 * 以线程为作用域
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
}
