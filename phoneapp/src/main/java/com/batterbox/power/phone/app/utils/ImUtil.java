package com.batterbox.power.phone.app.utils;

import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

/**
 * Created by ass on 2020-05-02.
 * Description
 */
public class ImUtil {

    public static void login() {
        if (UserUtil.isLogin()) {
            HttpClient.getInstance().im_getSign(new NormalHttpCallBack<ResponseEntity<String>>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<String> responseEntity) {
                    String userId = UserUtil.getUserId();
                    if (!StringUtil.isEmpty(userId)) {
                        String data = responseEntity.getData();
                        if (!StringUtil.isEmpty(data)) {
                            TUIKit.login(userId, data, new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {

                                    String msg = "im登陆成功--";
                                    FQL.d("TUIKit", msg);
//                                    FQT.showShort(BatterBoxApp.getInstance(), msg);
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                    FQL.d("TUIKit", "module=" + module + ";errCode==" + errCode + ";errMsg==" + errMsg);
//                                    FQT.showShort(BatterBoxApp.getInstance(), "im登陆失败--" + errMsg);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFail(ResponseEntity<String> responseEntity, String msg) {

                }
            });
        }
    }
}
