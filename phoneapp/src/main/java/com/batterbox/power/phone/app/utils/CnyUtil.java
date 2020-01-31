package com.batterbox.power.phone.app.utils;

import android.content.Context;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by ass on 2018/3/27.
 */

//TODO 费率处理
public class CnyUtil {


    public static String getPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(dfs);
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(price);
    }

    private static String getUnit(Context context) {
        return context == null ? "€" : context.getString(R.string.m_1);
    }

    public static String getPriceByUnit(Context context, String price, boolean ignoreCny) {
        if (ignoreCny)
            return getPrice(price) + "€";
        else
            return getPriceByUnit(context, price);
    }

    public static String getPriceByUnit(Context context, double price) {
        return getUnit(context) + " " + getPrice(price);
    }


    public static String getPrice(String price) {
        return getPrice(StringUtil.stringToDouble(price));
    }


    public static String getPriceByUnit(Context context, String price) {
        return getUnit(context) + " " + getPrice(price);
    }


}
