package com.batterbox.power.phone.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.batterbox.power.phone.app.act.WelcomeActivity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.ImUtil;
import com.batterbox.power.phone.app.utils.RefreshLanguageHelper;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.app.AppContextBase;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.tencent.imsdk.TIMBackgroundParam;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

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
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        register_user_push();
        RefreshLanguageHelper.init(this);


        //判断是否是在主线程
        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            TUIKit.addIMEventListener(new IMEventListener() {
                @Override
                public void onConnected() {
                    super.onConnected();
                    ImUtil.login();
                }
            });
            /**
             * TUIKit的初始化函数
             *
             * @param context  应用的上下文，一般为对应应用的ApplicationContext
             * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
             * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
             */
            TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());

//            if (ThirdPushTokenMgr.USER_GOOGLE_FCM) {
//                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(Task<InstanceIdResult> task) {
//                                if (!task.isSuccessful()) {
//                                    DemoLog.w(TAG, "getInstanceId failed exception = " + task.getException());
//                                    return;
//                                }
//
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//                                DemoLog.i(TAG, "google fcm getToken = " + token);
//
//                                ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
//                            }
//                        });
//            } else if (IMFunc.isBrandXiaoMi()) {
//                // 小米离线推送
//                MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
//            } else if (IMFunc.isBrandHuawei()) {
//                // 华为离线推送
//                HMSAgent.init(this);
//            } else if (MzSystemUtils.isBrandMeizu(this)) {
//                // 魅族离线推送
//                PushManager.register(this, PrivateConstants.MZ_PUSH_APPID, PrivateConstants.MZ_PUSH_APPKEY);
//            } else if (IMFunc.isBrandVivo()) {
//                // vivo离线推送
//                PushClient.getInstance(getApplicationContext()).initialize();
//            } else if (com.heytap.mcssdk.PushManager.isSupportPush(this)) {
//                // oppo离线推送，因为需要登录成功后向我们后台设置token，所以注册放在MainActivity中做
//            }

            registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());

        }


    }

    class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;
        private IMEventListener mIMEventListener = new IMEventListener() {
            @Override
            public void onNewMessages(List<TIMMessage> msgs) {
//                if (CustomMessage.convert2VideoCallData(msgs) != null) {
//                    // 会弹出接电话的对话框，不再需要通知
//                    return;
//                }
                for (TIMMessage msg : msgs) {
                    // 小米手机需要在设置里面把demo的"后台弹出权限"打开才能点击Notification跳转。TIMOfflinePushNotification后续不再维护，如有需要，建议应用自己调用系统api弹通知栏消息。
                    TIMOfflinePushNotification notification = new TIMOfflinePushNotification(BatterBoxApp.this, msg);
                    notification.doNotify(BatterBoxApp.this, R.drawable.default_user_icon);
                }

            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
//            DemoLog.i(TAG, "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // 若bundle不为空则程序异常结束
                // 重启整个程序
                Intent intent = new Intent(activity, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
//                DemoLog.i(TAG, "application enter foreground");
                TIMManager.getInstance().doForeground(new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
//                        DemoLog.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
//                        DemoLog.i(TAG, "doForeground success");
                    }
                });
                TUIKit.removeIMEventListener(mIMEventListener);
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // 应用切到后台
//                DemoLog.i(TAG, "application enter background");
                int unReadCount = 0;
                List<TIMConversation> conversationList = TIMManager.getInstance().getConversationList();
                for (TIMConversation timConversation : conversationList) {
                    unReadCount += timConversation.getUnreadMessageNum();
                }
                TIMBackgroundParam param = new TIMBackgroundParam();
                param.setC2cUnread(unReadCount);
                TIMManager.getInstance().doBackground(param, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
//                        DemoLog.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
//                        DemoLog.i(TAG, "doBackground success");
                    }
                });
                // 应用退到后台，消息转化为系统通知
                TUIKit.addIMEventListener(mIMEventListener);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public static void register_user_push() {
        if (UserUtil.isLogin()) {
            UserEntity userEntity = UserUtil.getUserInfo();
            if (userEntity != null) {
                JPushInterface.setAlias(getInstance(), 1000, String.valueOf(userEntity.mId));
                String registrationID = JPushInterface.getRegistrationID(getInstance());
                FQL.d("registrationID ==" + registrationID);
                if (!StringUtil.isEmpty(registrationID)) {
                    HttpClient.getInstance().us_updateJiguangId(registrationID, new NormalHttpCallBack<ResponseEntity>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(ResponseEntity responseEntity) {

                        }

                        @Override
                        public void onFail(ResponseEntity responseEntity, String msg) {

                        }
                    });
                }
            }
        }
    }

    public static void clean_user_push() {
        JPushInterface.setAlias(getInstance(), 1000, "");
        JPushInterface.deleteAlias(getInstance(), 1000);
    }
}
