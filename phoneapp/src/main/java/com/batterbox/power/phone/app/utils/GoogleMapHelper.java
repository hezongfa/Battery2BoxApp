package com.batterbox.power.phone.app.utils;

import com.chenyi.baselib.utils.MathsUtil;
import com.chenyi.baselib.utils.print.FQL;

public class GoogleMapHelper {

    private static double EARTH_RADIUS = 6378.137;    //地球半径

    //将用角度表示的角转换为近似相等的用弧度表示的角 Math.toRadians
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 谷歌地图计算两个坐标点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 距离（千米）
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
//	    s = Math.round(s * 10000) / 10000;
        s = MathsUtil.round(s, 2);
        FQL.d("xxxx", "haidistance lat1=" + lat1 + " lng1=" + lng1 + " lat2=" + lat2 + " lng2=" + lng2 + " dis=" + s);
        return s;
    }

    public static void main(String[] args) {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            getDistance(116.403933, 39.914147, 116.403237, 39.927919);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - b) + "毫秒"); //耗时：461毫秒
        double dist = getDistance(116.403933, 39.914147, 116.403237, 39.927919);
        System.out.println("两点相距：" + dist + "千米");    //两点相距：1.0千米

    }
}