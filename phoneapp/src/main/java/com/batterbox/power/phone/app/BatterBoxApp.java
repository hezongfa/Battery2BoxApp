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
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.qcloud.tim.uikit.TIMOfflinePushNotification;
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


        //???????????????????????????
        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            TUIKit.addIMEventListener(new IMEventListener() {
                @Override
                public void onConnected() {
                    super.onConnected();
                    ImUtil.login();
                }
            });
            /**
             * TUIKit??????????????????
             *
             * @param context  ?????????????????????????????????????????????ApplicationContext
             * @param sdkAppID ???????????????????????????????????????sdkAppID
             * @param configs  TUIKit?????????????????????????????????????????????????????????????????????API??????
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
//                // ??????????????????
//                MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
//            } else if (IMFunc.isBrandHuawei()) {
//                // ??????????????????
//                HMSAgent.init(this);
//            } else if (MzSystemUtils.isBrandMeizu(this)) {
//                // ??????????????????
//                PushManager.register(this, PrivateConstants.MZ_PUSH_APPID, PrivateConstants.MZ_PUSH_APPKEY);
//            } else if (IMFunc.isBrandVivo()) {
//                // vivo????????????
//                PushClient.getInstance(getApplicationContext()).initialize();
//            } else if (com.heytap.mcssdk.PushManager.isSupportPush(this)) {
//                // oppo???????????????????????????????????????????????????????????????token?????????????????????MainActivity??????
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
                FQL.d("onNewMessages",msgs.toString());
//                if (CustomMessage.convert2VideoCallData(msgs) != null) {
//                    // ???????????????????????????????????????????????????
//                    return;
//                }
                for (TIMMessage msg : msgs) {
                    TIMConversationType type = msg.getConversation().getType();
                    if (type == TIMConversationType.System|| type == TIMConversationType.Invalid) {
                       continue;
                    }
                    // ????????????????????????????????????demo???"??????????????????"??????????????????Notification?????????TIMOfflinePushNotification??????????????????????????????????????????????????????????????????api?????????????????????
                    TIMOfflinePushNotification notification = new TIMOfflinePushNotification(BatterBoxApp.this, msg);
                    notification.doNotify(BatterBoxApp.this, R.drawable.default_user_icon);
                }

            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
//            DemoLog.i(TAG, "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // ???bundle??????????????????????????????
                // ??????????????????
                Intent intent = new Intent(activity, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // ??????????????????
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
                // ??????????????????
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
                // ????????????????????????????????????????????????
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
        FQL.d("registrationID =register_user_push");
        if (UserUtil.isLogin()) {
            UserEntity userEntity = UserUtil.getUserInfo();
            FQL.d("registrationID =userEntity=" + userEntity);
            if (userEntity != null) {
                JPushInterface.setAlias(getInstance(), (int) (System.currentTimeMillis()/1000), String.valueOf(userEntity.mId));
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
