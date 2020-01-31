package com.chenyi.baselib.app;

import android.support.multidex.MultiDexApplication;

import com.chenyi.baselib.utils.ACache;
import com.chenyi.baselib.utils.SharedPreferencesUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.sys.SysUtil;
import com.chenyi.tao.shop.baselib.BuildConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tendcloud.tenddata.TCAgent;


public class AppContextBase extends MultiDexApplication {

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context)
//                        .setFinishDuration(30)
                    .setTextSizeTitle(13).setTextSizeTime(13)
                    .setEnableLastTime(false);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> new LoadingView(context));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20).setTextSizeTitle(13);
        });

    }

    private static AppContextBase _instance;

    public static AppContextBase getInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        SharedPreferencesUtil.initInstance(_instance);
        initTD();
    }
    //TalkingData
    protected void initTD() {
        TCAgent.LOG_ON = BuildConfig.DEBUG;
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(!BuildConfig.DEBUG);
    }
//    public String imei;

    public String getImei() {
        String imei = SysUtil.getIMEI(this);
        FQL.d("imei=" + imei);
        return imei;
//        if (imei == null) {
//            imei = SysUtil.getIMEI(this);
//        }
//        FQL.d("imei=" + imei);
//        //TODO
//        return imei;
//        return "02:00:00:00:00:00";
    }

    //TalkingData
//    protected void initTD() {
//        TCAgent.LOG_ON = BuildConfig.DEBUG;
//        TCAgent.init(this);
//        TCAgent.setReportUncaughtExceptions(!BuildConfig.DEBUG);
//    }


    private ACache constCache;

    /**
     * 通用缓存
     *
     * @return
     */
    public ACache getConstACache() {
        if (constCache == null)
            constCache = ACache.get(this, "ConstCache");
        return constCache;
    }
}
