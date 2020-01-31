package com.chenyi.baselib.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by ass on 2018/12/4.
 * Description
 */
public abstract class NavigationActivity extends BaseNavActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.post(() -> onDelayLoad(savedInstanceState));
    }

    protected static Handler handler = new Handler();

    /**
     * 需要延迟加载的东西放到该方法中执行
     * 1、获取Intent数据耗时较短，并且会影响后续操作，应放在OnCreate方法中执行
     * 2、最基本的、保证用户体验的界面渲染应放在OnCreate方法中执行
     * 3、涉及数据库查询更改的操作耗时较长，应放在onDelayLoad中执行
     * 4、涉及列表加载、Adapter渲染的操作耗时较长，应放在onDelayLoad中执行
     * 5、控件的监听事件应保证所有界面加载好后再设置，防止异常发生，应放在onDelayLoad中执行
     * 6、其他未提及的耗时操作应视实际情况，尽可能地放在onDelayLoad中执行
     */
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {

    }
}
