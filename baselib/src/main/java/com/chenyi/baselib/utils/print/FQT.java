package com.chenyi.baselib.utils.print;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类 T.java
 *
 * @author zongfa_he
 * @description
 * @since 2014-8-7 上午11:36:05
 */
public class FQT {
    // Toast
//    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
//        if (null == toast) {
//            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//            // toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(message);
//        }
//        toast.show();
    }

    /**
     * 短时间显示Toast
     *  @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
//        if (null == toast) {
//            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//            // toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(message);
//        }
//        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
//        if (null == toast) {
//            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//            // toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(message);
//        }
//        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        show(context, message, Toast.LENGTH_LONG);
//        if (null == toast) {
//            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//            // toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(message);
//        }
//        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

}
