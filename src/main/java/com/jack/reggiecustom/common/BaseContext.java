package com.jack.reggiecustom.common;

/**
 * 基于ThreadLocal的工具类，用于保存和获取当前用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //因为是工具类，所以都是public static
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
