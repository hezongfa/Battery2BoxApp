package com.chenyi.baselib.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chenyi.baselib.utils.LanguageContextWrapper;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ass on 2018/11/28.
 * Description
 */
public abstract class BaseActivity extends AppCompatActivity {
    Object subscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, LanguageUtil.getLocaleByLanguage(StringUtil.fixNullStr(LanguageUtil.getLanguage(), LanguageUtil.ES))));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, getClass().getName());
    }

    @Override
    protected void onDestroy() {
        unRegisterEventBus();
        super.onDestroy();
    }
//    protected abstract Class<T> getViewModel();

    protected void registerEventBus(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        this.subscriber = subscriber;
        EventBus.getDefault().register(subscriber);
    }

    protected void unRegisterEventBus() {
        if (subscriber != null && EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }
}
