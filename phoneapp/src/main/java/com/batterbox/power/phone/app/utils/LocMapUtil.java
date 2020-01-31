package com.batterbox.power.phone.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.batterbox.power.phone.app.entity.LocMap;
import com.chenyi.baselib.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Zongfa.He
 */
public class LocMapUtil {

    // public static int TYPE_GOOGLEMAP = 0;
    // public static int TYPE_BAIDUMAP = 1;
    // public static int TYPE_AUTONAVIMAP = 2;

    // public void navigationByLocMap(Coordinate mCoordinate, int TYPE_MAP) {
    // switch (TYPE_MAP) {
    // case 0:
    // navigationByGoogle(mCoordinate);
    // break;
    // case 1:
    // navigationByBaidu(mCoordinate);
    // break;
    // case 2:
    // navigationByAutonavi(mCoordinate);
    // break;
    // default:
    // break;
    // }
    // }

    public static void navigationByLocMap(Context context, double lat, double lng, String title, String address, String TYPE_MAP) {
        if (TYPE_MAP.equals(BAIDUMAPPGNAME))
            navigationByBaidu(context, lat, lng, title, address);
        if (TYPE_MAP.equals(GOOGLEMAPPGNAME))
            navigationByGoogle(context, lat, lng, address);
        if (TYPE_MAP.equals(AUTONAVIMAPPGNAME))
            navigationByAutonavi(context, lat, lng, address);
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应�?
     */
    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static final String BAIDUMAPPGNAME = "com.baidu.BaiduMap";
    public static final String GOOGLEMAPPGNAME = "com.google.android.apps.maps";
    public static final String AUTONAVIMAPPGNAME = "com.autonavi.minimap";

    /**
     * @see String 类型 的三位正整数，每位整数为�?”或�?�?�?
     * 格式：第�?��整数表示是否安装了百度地图客户端�?第二个整数表示是否安装了谷歌地图客户端； 第三个整数表示是否安装了高德地图客户端；
     * �?”表示未安装，�?1”表示已安装
     */
    private static String getInstalledMap() {
        String locMapsState = "";
        if (isInstallByread(BAIDUMAPPGNAME)) {
            locMapsState += "1";
        } else {
            locMapsState += "0";
        }
        if (isInstallByread(GOOGLEMAPPGNAME)) {
            locMapsState += "1";
        } else {
            locMapsState += "0";
        }
        if (isInstallByread(AUTONAVIMAPPGNAME)) {
            locMapsState += "1";
        } else {
            locMapsState += "0";
        }
        return locMapsState;
    }

    public static List<LocMap> getLocMapList(Context context) {
        String locMapsState = getInstalledMap();
        List<LocMap> locmaplist = new ArrayList<LocMap>();
        if (locMapsState.equals("000")) {
            return locmaplist;
        } else {
            if (locMapsState.equals("111")) {
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.BAIDUMAPPGNAME));
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.GOOGLEMAPPGNAME));
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.AUTONAVIMAPPGNAME));
            }
            if (locMapsState.equals("100"))
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.BAIDUMAPPGNAME));
            if (locMapsState.equals("010"))
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.GOOGLEMAPPGNAME));
            if (locMapsState.equals("001"))
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.AUTONAVIMAPPGNAME));
            if (locMapsState.equals("110")) {
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.BAIDUMAPPGNAME));
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.GOOGLEMAPPGNAME));
            }
            if (locMapsState.equals("101")) {
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.BAIDUMAPPGNAME));
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.AUTONAVIMAPPGNAME));
            }
            if (locMapsState.equals("011")) {
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.GOOGLEMAPPGNAME));
                locmaplist
                        .add(getPackageIcon_AppName(context, LocMapUtil.AUTONAVIMAPPGNAME));
            }
            return locmaplist;
        }
    }

    public static LocMap getPackageIcon_AppName(Context context, String packageName) {
        LocMap mLocMap = new LocMap();
        PackageManager pm = context.getPackageManager();
        Drawable drawable = null;
        try {
            drawable = pm.getApplicationIcon(packageName);
            ApplicationInfo mApplicationInfo = pm.getApplicationInfo(
                    packageName, 0);
            String appName = mApplicationInfo.loadLabel(pm).toString();
            mLocMap.packageName = packageName;
            mLocMap.icon = drawable;
            mLocMap.appName = appName;
            // System.out.println(mApplicationInfo.loadLabel(pm));
            return mLocMap;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "找不到包名的应用程序", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 使用百度地图客户端
     */
    private static void navigationByBaidu(Context context, double lat, double lng, String title, String address) {
        // String type="wgs84";
        String type = "gcj02";
        try {
            // Intent intent =
            // Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁�?mode=driving®ion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
//            Intent intent = Intent.getIntent("intent://map/marker?location="
//                    + lat + ","
//                    + lng + "&title="
//                    + StringUtil.fixNullStr(address) + "&coord_type=" + type
//                    + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");// &src=yourCompanyName|yourAppName
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("baidumap://map/marker?location=" + lat + "," + lng + "&title=" + StringUtil.fixNullStr(address) + "&content=" + StringUtil.fixNullStr(address) + "&traffic=on&src=andr.baidu.openAPIdemo"));
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/marker?location=" + lat + "," + lng + "&title=" + StringUtil.fixNullStr(title) + "&content=" + StringUtil.fixNullStr(address) + "&traffic=on&src=andr.baidu.openAPIdemo"));
            context.startActivity(intent); // 启动调用
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "找不到百度地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用高德地图客户端导�?
     */
    private static void navigationByAutonavi(Context context, double lat, double lng, String address) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse("androidamap://viewMap?sourceApplication="
                            + "app测试"
                            + "&poiname=" + StringUtil.fixNullStr(address) + "&lat="
                            + lat + "&lon="
                            + lng + "&dev=0"));
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "找不到高德地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用谷歌地图客户端导�?
     */
    private static void navigationByGoogle(Context context, double lat, double lng, String address) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
                            + lat + ","
                            + lng + "("
                            + StringUtil.fixNullStr(address) + ")"));

            // 从哪到哪的路�?
            // Intent i = new
            // Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="
            // + chufaweidu
            // + ","
            // + chufajingdu
            // + "&daddr="
            // + daodaweidu
            // + ","
            // + daodajingdu + "&hl=zh"));
            // 如果强制使用googlemap地图客户端打�?��就加下面两句
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            context.startActivity(i);
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "找不到谷歌地图客户端", Toast.LENGTH_SHORT).show();
        }
    }


//    /**
//     * 百度摩卡拖坐标与火星坐标的加密解密算法
//     *
//     * @author XFan
//     */
//    private static double lat = 31.22997;
//    private static double lon = 121.640756;
//
//    public static double getX_pi(double lat, double lon) {
//        return lat * lon / 180.0;
//    }

//    /**
//     * 摩卡托坐标（百度坐标）解密成为火星坐标
//     *
//     * @return
//     */
//    public static Coordinate bd_decrypt(Coordinate coordinate) {
//        double x = coordinate.getLongitude() - 0.0065, y = coordinate.getLatitude() - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * getX_pi(lat, lon));
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * getX_pi(lat, lon));
//        double gg_lon = z * Math.cos(theta);
//        double gg_lat = z * Math.sin(theta);
//        coordinate.setLatitude(gg_lat);
//        coordinate.setLongitude(gg_lon);
//        return coordinate;
//    }
//
//    /**
//     * 加密成为摩卡托坐标（百度坐标）
//     *
//     * @return
//     */
//    public static Coordinate bd_encrypt(Coordinate coordinate) {
//        double x = coordinate.getLongitude(), y = coordinate.getLatitude();
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * getX_pi(lat, lon));
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * getX_pi(lat, lon));
//        double bd_lon = z * Math.cos(theta) + 0.0065;
//        double bd_lat = z * Math.sin(theta) + 0.006;
//        coordinate.setLatitude(bd_lat);
//        coordinate.setLongitude(bd_lon);
//        return coordinate;
//    }
}
