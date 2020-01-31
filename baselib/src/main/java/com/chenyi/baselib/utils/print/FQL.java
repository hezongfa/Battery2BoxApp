package com.chenyi.baselib.utils.print;

import android.util.Log;

/**
 * Log统一管理类 L.java
 * 
 * @author zongfa_he
 * @version
 * @since 2014-8-7 上午11:35:23
 * @description
 * 
 */
public class FQL {
    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "FQL";
    
    public static void setDebug(boolean isDebug) {
        FQL.isDebug = isDebug;
    }
    
    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }
    
    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }
    
    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }
    
    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }
    
    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }
    
    // 下面是传入类名打印log
    public static void i(Class<?> _class, String msg) {
        if (isDebug)
            Log.i(_class.getName(), msg);
    }
    
    public static void d(Class<?> _class, String msg) {
        if (isDebug)
            Log.d(_class.getName(), msg);
    }
    
    public static void e(Class<?> _class, String msg) {
        if (isDebug)
            Log.e(_class.getName(), msg);
    }
    
    public static void w(Class<?> _class, String msg) {
        if (isDebug)
            Log.w(_class.getName(), msg);
    }
    
    public static void v(Class<?> _class, String msg) {
        if (isDebug)
            Log.v(_class.getName(), msg);
    }
    
    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }
    
    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }
    
    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }
    
    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }
    
    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
