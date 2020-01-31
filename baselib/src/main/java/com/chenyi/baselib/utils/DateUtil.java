package com.chenyi.baselib.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    /**
     * 转换时间格式
     *
     * @param timestamp
     * @param format    MM-DD
     * @return
     */
    public static String getStringByFormat(long timestamp, String format) {
        // Log.d("getStandardTime", "getStandardTime-----" + timestamp);
        if (timestamp != 0)
            return DateFormat.format(format, timestamp).toString();
        return "";
    }

    /**
     * 格式化时间转换为毫秒单位时间
     *
     * @param time
     * @param format
     * @return
     */
    public static long timeformat2million(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long milliontime = -1L;
        try {
            long millionSeconds = sdf.parse(time).getTime();
            milliontime = millionSeconds;// 毫秒
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return milliontime;
    }

    public static String getShowTime(String timeStr) {
        if (StringUtil.isEmpty(timeStr)) {
            return "";
        } else {
            long time = DateUtil.timeformat2million(timeStr, "yyyy-MM-dd HH:mm:ss");
            if (time == 0) {
                return "刚刚";
            } else {
                Calendar localCalendar = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                long localTime = localCalendar.getTimeInMillis();
                long diff = localTime - time;
                if (diff < 60 * 1000L) {
                    return "刚刚";
                } else {
                    if (diff < 60 * 60 * 1000L) {//一小时
                        long dat = diff / 1000 / 60;
                        return dat + "分钟前";
                    } else if (diff < 24 * 60 * 60 * 1000L) {//一天
                        long dat = diff / 1000 / 60 / 60;
                        return dat + "小时前";
                    } else if (diff < 30 * 24 * 60 * 60 * 1000L) {//粗略30天为一个月
                        long dat = diff / 1000 / 60 / 60 / 24;
                        return dat + "天前";
                    } else if (diff < 365 * 24 * 60 * 60 * 1000L) {//一年
                        long dat = diff / 1000 / 60 / 60 / 24 / 30;
                        return dat + "月前";
                    } else {
                        long dat = diff / 1000 / 60 / 60 / 24 / 30 / 365;
//                        return dat + "年前";
                        return DateUtil.getStringByFormat(dat, "yyyy-MM-dd");
                    }
                }
            }
        }
    }

    public static String getShowTime(long time) {
        if (time == 0) {
            return "刚刚";
        } else {
            Calendar localCalendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            long localTime = localCalendar.getTimeInMillis();
            long diff = localTime - time;
            if (diff < 60 * 1000L) {
                return "刚刚";
            } else {
                if (diff < 60 * 60 * 1000L) {//一小时
                    long dat = diff / 1000 / 60;
                    return dat + "分钟前";
                } else if (diff < 24 * 60 * 60 * 1000L) {//一天
                    long dat = diff / 1000 / 60 / 60;
                    return dat + "小时前";
                } else if (diff < 30 * 24 * 60 * 60 * 1000L) {//粗略30天为一个月
                    long dat = diff / 1000 / 60 / 60 / 24;
                    return dat + "天前";
                } else if (diff < 365 * 24 * 60 * 60 * 1000L) {//一年
                    long dat = diff / 1000 / 60 / 60 / 24 / 30;
                    return dat + "月前";
                } else {
                    long dat = diff / 1000 / 60 / 60 / 24 / 30 / 365;
                    return dat + "年前";
                }
            }
        }
    }


    public static String getShowYMD(String date, String format) {
        if (date == null) return null;
        long time = timeformat2million(date, format);
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(time);
        int dateYear = calendar.get(Calendar.YEAR);
        int dateMonth = calendar.get(Calendar.MONTH);
        if (curYear == dateYear) {
            if (curMonth == dateMonth) {
                return "本月";
            } else {
                return dateMonth + "月";
            }
        } else {
            return getStringByFormat(time, "yyyy-MM-dd");
        }
    }
}
