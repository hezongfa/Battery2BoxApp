package com.batterbox.power.phone.app.http;

import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.http.HttpCallBack;
import com.chenyi.baselib.utils.http.IHttpView;
import com.chenyi.baselib.widget.dialog.HttpRotateLoadingDialog;

/**
 * Created by ass on 2019/1/8.
 * Description
 */
public abstract class NormalHttpCallBack<T extends ResponseEntity> extends HttpCallBack<T> {
    public NormalHttpCallBack() {
    }

    public NormalHttpCallBack(IHttpView iHttpView) {
        super(iHttpView);
    }

    public NormalHttpCallBack(Object target) {
        super(new HttpRotateLoadingDialog(), target);
    }

    public NormalHttpCallBack(IHttpView iHttpView, Object target) {
        super(iHttpView, target);
    }

    boolean ignoreLogin;

    public HttpCallBack<T> setIgnoreLogin(boolean ignoreLogin) {
        this.ignoreLogin = ignoreLogin;
        return this;
    }

    @Override
    protected void onNextFail(T responseEntity) {
        if (!ignoreLogin && responseEntity.isToLogin()) {
//            FQT.showShort(ShopkeeperApp.getInstance(), text(ShopkeeperApp.getInstance(), R.string.app_18));
            UserUtil.cleanToken();
            UserUtil.cleanUserInfo();
            ARouteHelper.login().navigation();
        }
        onFail(responseEntity, StringUtil.fixNullStr(responseEntity.getMsg(), "error"));
    }
}
