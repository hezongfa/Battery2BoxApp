package com.batterbox.power.phone.app;

import com.chenyi.baselib.utils.SharedPreferencesUtil;

public class AppConfigUtil {
    public static boolean isFirstLaunch() {
        return SharedPreferencesUtil.getInstance().getBoolean("isFirstLaunch", true);
    }

    public static void setFirstLaunch(boolean isFirstLaunch) {
        SharedPreferencesUtil.getInstance().saveBoolean("isFirstLaunch", isFirstLaunch);
        setFirstLaunchByVersion(isFirstLaunch);
    }

    public static boolean isFirstLaunchByVersion() {
        return SharedPreferencesUtil.getInstance().getBoolean("isFirstLaunchByVersion_" + BuildConfig.VERSION_NAME, true);
    }

    private static void setFirstLaunchByVersion(boolean isFirstLaunchByVersion) {
        SharedPreferencesUtil.getInstance().saveBoolean("isFirstLaunchByVersion_" + BuildConfig.VERSION_NAME, isFirstLaunchByVersion);
    }

}
