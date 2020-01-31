package com.chenyi.baselib.utils.sys;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.chenyi.baselib.app.AppContextBase;
import com.chenyi.baselib.utils.print.FQL;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;

public class SysUtil {


    static String TAG = SysUtil.class.getName();

    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT >= 21) {
//            List<ActivityManager.AppTask> info = activityManager.getAppTasks();
//            if (info != null && info.size() > 0) {
//                ActivityManager.RecentTaskInfo recentTaskInfo = info.get(0).getTaskInfo();
//                ComponentName name = recentTaskInfo.origActivity;
//                Log.d("xx", "LOLLIPOP__component.getClassName()--" + name.getClassName());
//                if (activityClassName.equals(name.getClassName())) {
//                    return true;
//                }
//            }
//        } else {
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
//        }
        return false;
    }

    /**
     * 判断栈中有无
     *
     * @param mContext
     * @param activityClassName
     * @return
     */
    public static boolean isActivityOnTask(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT >= 21) {
//            List<ActivityManager.AppTask> info = activityManager.getAppTasks();
//            if (info != null && info.size() > 0) {
//                ActivityManager.RecentTaskInfo recentTaskInfo = info.get(0).getTaskInfo();
//                ComponentName name = recentTaskInfo.origActivity;
//                Log.d("xx", "LOLLIPOP__component.getClassName()--" + name.getClassName());
//                if (activityClassName.equals(name.getClassName())) {
//                    return true;
//                }
//            }
//        } else {
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(15);
        if (info != null && info.size() > 0) {
            for (ActivityManager.RunningTaskInfo fo : info) {

                if (activityClassName.equals(fo.topActivity.getClassName())) {
                    return true;
                }
            }
//            ComponentName component = info.get(0).topActivity;
//            Log.d("xx", "component.getClassName()--" + component.getClassName());
//            if (activityClassName.equals(component.getClassName())) {
//                return true;
//            }
        }
//        }
        return false;
    }

    public static void openUrl(Context context, String urlStr) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(urlStr);
//        intent.setData(content_url);
//        context.startActivity(intent);
        try {
            Uri uri = Uri.parse(urlStr);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            FQL.e(e.getMessage());
        }

    }

    public static void openUrlSelectWeb(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, "选择"));
    }

    public static void callTel(Context context, String telNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    public static String getIMEI(Context context) {
//        String imei = "";
//        TelephonyManager telephonemanage = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        try {
//            imei = telephonemanage.getDeviceId();
//            return imei;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FQL.d(TAG, "getIMEI=" + imei);
//        return imei;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = telephonyManager.getClass();
        Method getImei;
        try {
            getImei = clazz.getDeclaredMethod("getImei", int.class);//(int slotId)
//            getImei.invoke(telephonyManager, 0); //卡1
//            getImei.invoke(telephonyManager, 1); // 卡2
            String imei = (String) getImei.invoke(telephonyManager, 0);
            FQL.e(TAG, "IMEI1 : " + getImei.invoke(telephonyManager, 0)); //卡1
            return imei;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getIccid(Context context) {
        String phone = "";
        TelephonyManager telephonemanage = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            phone = telephonemanage.getSimSerialNumber();
            return phone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // L.d(TAG, "getIMEI---IMEI=" + imei);
        return phone;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        // L.d(TAG, "getLocalIpAddress---HostAddress="
                        // + inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e("WifiPreference IpAddress", ex.toString());
        } catch (Exception ex) {
        }
        return "";
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getProductModel() {
        try {
            return Build.MODEL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getProductVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static int getProductAPIVersion() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static SensorManager getSensorManager(Context context) {

        SensorManager sm = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        return sm;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前看到的activity名称
     *
     * @param context
     * @return
     */
    private String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }


    private static final String SCHEME = "package";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
     */
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";
    /**
     * InstalledAppDetails所在包名
     */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    /**
     * InstalledAppDetails类名
     */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    /**
     * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
     * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
     *
     * @param context
     * @param packageName 应用程序的包名
     */
    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }

    /**
     * 复制到剪切板
     *
     * @param context
     * @param str
     */
    public static void copyStr(Context context, String str) {
        if (Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("temp", str));

        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setText(str);
        }

    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


    public static String getMetaString(String name) {
        Bundle metaData = getMetaData();
        return metaData == null ? "" : metaData.getString(name);
    }

    private static Bundle getMetaData() {
        try {
            ApplicationInfo ai = AppContextBase.getInstance().getPackageManager().getApplicationInfo(
                    AppContextBase.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData;
        } catch (Exception e) {
//            LogUtils.printStackTrace(e);
        }
        return null;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static boolean returnResult(int value) {
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    /**
     * 114      * 静默卸载
     * 115
     */
    public static boolean clientUninstall(String packageName) {

        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall " + packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    public static boolean rootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.e("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                Log.e("*** DEBUG ***", "ROOT finally" + e.getMessage());
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    public static void restartDevice() {
        FQL.d("MWindowService --- rebootAction");
        rootCommand("am restart");
    }

    public static void setMobileData(Context pContext, boolean pBoolean) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
            method.invoke(mConnectivityManager, pBoolean);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

