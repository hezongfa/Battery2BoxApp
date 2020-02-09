package com.batterbox.power.phone.app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.batterbox.power.phone.app.utils.RefreshLanguageHelper;
import com.chenyi.baselib.app.AppContextBase;
import com.chenyi.baselib.utils.LanguageUtil;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
public class BatterBoxApp extends AppContextBase {
    public static double lat;
    public static double lng;

    @Override
    public void onCreate() {
        super.onCreate();
        LanguageUtil.initLanguage(this);
        if (BuildConfig.DEBUG) {      // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        String registrationID = JPushInterface.getRegistrationID(this);
//        FQL.d("registrationID ==" + registrationID);
//        register_user_push();
        RefreshLanguageHelper.init(this);
    }

//    public static void register_user_push() {
//        if (UserUtil.isLogin()) {
//            UserEntity userEntity = UserUtil.getUserInfo();
//            if (userEntity != null) {
//                JPushInterface.setAlias(getInstance(), 1000, String.valueOf(userEntity.mId));
//            }
//        }
//    }
//    public static void clean_user_push(){
//        JPushInterface.setAlias(getInstance(), 1000, "");
//    }
}
