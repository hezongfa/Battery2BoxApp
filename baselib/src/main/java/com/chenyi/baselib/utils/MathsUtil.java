package com.chenyi.baselib.utils;

import java.math.BigDecimal;

public class MathsUtil {
    /**
     * 进行加法运算
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double add(double d1, double d2) { //
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }
    
    /**
     * 进行减法运算
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) { //
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * 进行乘法运算
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) { //
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 进行除法运算
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double div(double d1, double d2, int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 进行四舍五入操作
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double round(double d, int len) { // 进行四舍五入操作
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 进行0舍1入操作
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static double roundup(double d, int len) { //
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        return b1.divide(b2, len, BigDecimal.ROUND_UP).doubleValue();
    }

    public static float round(float d, int len) { // 进行四舍五入操作
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
