package com.chenyi.baselib.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public abstract class BaseFragment extends Fragment {
    protected ViewGroup contentView;
    protected BaseActivity baseActivity;
    boolean isInit = false;
    protected Resources resources;
    Object subscriber;
    public static <T extends Fragment> T newInstance(Context context, Class<T> clazz) {
        return newInstance(context, clazz, null);
    }

    public static <T extends Fragment> T newInstance(Context context, Class<T> clazz, Bundle args) {
        return (T) Fragment.instantiate(context, clazz.getName(), args);
    }
    @Override
    public void onResume() {
        super.onResume();
        TCAgent.onPageStart(getContext(), getClass().getName());
    }
    @Override
    public void onPause() {
        super.onPause();
        TCAgent.onPageEnd(getContext(), getClass().getName());
    }
    /**
     * 是否已初始化 init() 后  isInit=true
     *
     * @return
     */
    public boolean isInit() {
        return isInit;
    }
    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        contentView = (ViewGroup) inflater.inflate(getLayout(), null);
        baseActivity = (BaseActivity) getActivity();
        resources = getResources();
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
//        try {
            findView();
            if (getArguments() != null) {
                init(getArguments());
                new Handler().post(() -> new Handler()
                        .post(() -> onDelayLoad(getArguments())));
            } else {
                init(new Bundle());
                new Handler().post(() -> new Handler()
                        .post(() -> onDelayLoad(new Bundle())));
            }
            isInit = true;
//        } catch (Exception e) {
//            FQL.e("QBH_Exception", e.getMessage());
//        }

    }
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

//    public void replaceFragment(int resId, Fragment fragment) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(resId, fragment, fragment.getClass().getName());
//        transaction.commit();
//    }
//
//    public void replaceFragment(int resId, Fragment fragment, String tag) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(resId, fragment, tag);
//        transaction.commit();
//    }

    @Override
    public void onDestroyView() {
        unRegisterEventBus();
        super.onDestroyView();
    }
    protected void registerEventBus(Object subscriber) {
        if (subscriber == null) return;
        this.subscriber = subscriber;
        EventBus.getDefault().register(subscriber);
    }

    protected void unRegisterEventBus() {
        if (subscriber != null && EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }
    public  <T extends View> T  findViewById(int id) {
        return contentView.findViewById(id);
    }

//    protected View findViewWithTag(Object tag) {
//        return contentView.findViewWithTag(tag);
//    }

    protected abstract int getLayout();

    protected abstract void findView();

    protected abstract void init(Bundle bundle);

}
