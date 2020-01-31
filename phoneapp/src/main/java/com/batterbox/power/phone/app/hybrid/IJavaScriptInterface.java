package com.batterbox.power.phone.app.hybrid;

import android.webkit.JavascriptInterface;

/**
 * Created by ass on 2018/4/28.
 */

public interface IJavaScriptInterface {
    //    BaseActivity activity;
    public static final String JsObjectName = "INativeObject";

    //
//    public IJavaScriptInterface(BaseActivity activity) {
//        this.activity = activity;
//    }

    @JavascriptInterface
    String getLanguage();

    @JavascriptInterface
    void save_img64(String imgBase64);

    @JavascriptInterface
    void shopkeeper_log(String log);

    @JavascriptInterface
    String shopkeeper_getToken();

    @JavascriptInterface
    void shopkeeper_closeWindow();

    @JavascriptInterface
    void shopkeeper_share_file(String json);

    @JavascriptInterface
    void shopkeeper_share_link(String json);

    @JavascriptInterface
    void shopkeeper_down_file(String json);

    @JavascriptInterface
    void shopkeeper_share_imgs(String json);

    @JavascriptInterface
    void shopkeeper_user_agreement_agree();

    @JavascriptInterface
    void shopkeeper_user_agreement_refuse();


}
