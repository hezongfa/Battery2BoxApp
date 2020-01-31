package com.batterbox.power.phone.app.utils;

import com.chenyi.baselib.utils.DateUtil;

/**
 * Created by ass on 2019-08-07.
 * Description
 */
public class OrderHelper {

    public static String getShowTime(long time) {
        if (time < 60) {
            return time + "秒";
        } else if (time < 60 * 60) {
            return DateUtil.getStringByFormat(time * 1000, "mm分钟ss秒");
        } else if (time < 60 * 60 * 24) {
            return DateUtil.getStringByFormat(time * 1000, "hh时mm分ss秒");
        } else {
            return DateUtil.getStringByFormat(time * 1000, "d天hh时mm分ss秒");
        }
    }

}
