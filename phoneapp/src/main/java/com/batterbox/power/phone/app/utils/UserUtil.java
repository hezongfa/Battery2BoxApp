package com.batterbox.power.phone.app.utils;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.chenyi.baselib.app.AppContextBase;
import com.chenyi.baselib.utils.SharedPreferencesUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

public class UserUtil {

    public static String getUserId() {
        UserEntity userCenterDataEntity = getUserInfo();
        if (userCenterDataEntity != null) {
            return userCenterDataEntity.mId + "";
        }
        return null;
    }

    public static String TOKEN;

    public static void saveToken(String token) {
        TOKEN = token;
        SharedPreferencesUtil.getInstance().saveString("token", token);
    }

    public static String getToken() {
        if (StringUtil.isEmpty(TOKEN)) {
            return SharedPreferencesUtil.getInstance().getString("token", "");
        } else {
            return TOKEN;
        }
    }

    public static void cleanToken() {
        TOKEN = null;
        SharedPreferencesUtil.getInstance().remove("token");
//        JPushInterface.deleteAlias(BatterBoxApp.getInstance(), 111);
    }

    public static boolean checkLogin() {
        if (isLogin()) {
            return true;
        } else {
            gotoLogin();
            return false;
        }
    }

    public static void gotoLogin() {
        ARouteHelper.login().navigation();
    }

    public static boolean isLogin() {
        if (StringUtil.isEmpty(getToken())) {
            return false;
        }
        return true;
    }

    public static void saveUserInfo(UserEntity userEntity) {
        cleanUserInfo();
        AppContextBase.getInstance().getConstACache().put("userEntity", userEntity, 60 * 60 * 24 * 30);//30天
        BatterBoxApp.register_user_push();

    }

    public static UserEntity getUserInfo() {
        return (UserEntity) AppContextBase.getInstance().getConstACache().getAsObject("userEntity");
    }

    public static void cleanUserInfo() {
        AppContextBase.getInstance().getConstACache().remove("userEntity");
        BatterBoxApp.clean_user_push();
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

}
