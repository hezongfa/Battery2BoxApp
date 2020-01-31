package com.batterbox.power.phone.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.ui.BaseActivity;

import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;

/**
 * Created by ass on 2019-10-09.
 * Description
 */
public class ScanBarCodeHelper {
    public static QrConfig createConfig(Context context) {
        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText(context.getString(R.string.scan_1))//扫描框下文字
                .setShowDes(true)//是否显示扫描框下面文字
                .setShowLight(true)//显示手电筒按钮
                .setShowTitle(true)//显示Title
                .setShowAlbum(false)//显示从相册选择按钮
                .setNeedCrop(false)//是否从相册选择后裁剪图片
                .setCornerColor(ContextCompat.getColor(context, R.color.yellow_f0c517))//设置扫描框颜色
                .setLineColor(ContextCompat.getColor(context, R.color.yellow_f0c517))//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(QrConfig.TYPE_ALL)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(QrConfig.TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
//                    .setCustombarcodeformat(QrConfig.BARCODE_EAN13)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(true)//是否扫描成功后bi~的声音
//                    .setDingPath(cb_show_custom_ding.isChecked() ? R.raw.test : R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
//                    .setIsOnlyCenter(cb_only_center.isChecked())//是否只识别框中内容(默认为全屏识别)
                .setTitleText(context.getString(R.string.scan_2))//设置Tilte文字
                .setTitleBackgroudColor(ContextCompat.getColor(context, R.color.yellow_f0c517))//设置状态栏颜色
                .setTitleTextColor(Color.WHITE)//设置Title文字颜色
                .setShowZoom(false)//是否开始滑块的缩放
                .setAutoZoom(false)//是否开启自动缩放(实验性功能，不建议使用)
                .setFingerZoom(true)//是否开始双指缩放
                .setDoubleEngine(true)//是否开启双引擎识别(仅对识别二维码有效，并且开启后只识别框内功能将失效)
                .setScreenOrientation(QrConfig.SCREEN_PORTRAIT)//设置屏幕方式
                .setOpenAlbumText("选择要识别的图片")//打开相册的文字
                .setLooperScan(false)//是否连续扫描二维码
//                    .setLooperWaitTime(Integer.parseInt(et_loop_scan_time.getText().toString()) * 1000)//连续扫描间隔时间
                .setScanLineStyle(QrConfig.LINE_MEDIUM)//扫描线样式
                .setAutoLight(true)//自动灯光
                .create();
        return qrConfig;
    }

    public static void scan(BaseActivity activity,QrManager.OnScanResultCallback callback){
        QrManager.getInstance().init(createConfig(activity)).startScan(activity, callback);
    }
}
