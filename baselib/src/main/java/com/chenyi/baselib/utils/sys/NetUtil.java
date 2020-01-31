package com.chenyi.baselib.utils.sys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    /**
     * 判断是否连上网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        if (mobile == State.CONNECTED || wifi == State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否连上数据网络
     *
     * @param context
     * @return
     */
    public static boolean checkIsMobileNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        // State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        // .getState();

        if (mobile == State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否连上wifi
     *
     * @param context
     * @return
     */
    public static boolean checkIsWifiNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        // .getState();
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        if (wifi == State.CONNECTED) {
            return true;
        }
        return false;
    }
}
