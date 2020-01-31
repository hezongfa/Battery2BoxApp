package com.chenyi.baselib.utils.http;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.widget.dialog.NormalLoadingDialog;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by mac on 04/01/2018.
 */

public abstract class HttpCallBack<T extends ResponseEntity> implements Observer<T> {
    public Disposable d;
    public IHttpView iHttpView;
    public Object target;


    public HttpCallBack() {

    }

    public HttpCallBack(IHttpView iHttpView) {
        this.iHttpView = iHttpView;
    }

    public HttpCallBack(Object target) {
        this(new NormalLoadingDialog(), target);
    }

    public HttpCallBack(IHttpView iHttpView, Object target) {
        this.iHttpView = iHttpView;
        this.target = target;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (iHttpView != null && checkTargetEnable())// && target != null
            iHttpView.onIHttpViewShow(target);
        onStart();
    }

    @Override
    public void onNext(T responseEntity) {
        if (responseEntity == null) return;
        int successCode = 10000;
        if (responseEntity.getCode() == successCode) {
            onSuccess(responseEntity);
        } else {
            onNextFail(responseEntity);
        }
        if (iHttpView != null && checkTargetEnable())
            iHttpView.onIHttpViewClose();
    }


    @Override
    public void onError(Throwable e) {
//        if (iHttpView == null || !iHttpView.isViewCanceled()) {
        String msg = "请求失败";
        if (e instanceof UnknownHostException) {
            msg = "请检查网络设置";
        }
        onFail(null, msg);
//        }
        if (iHttpView != null && checkTargetEnable())
            iHttpView.onIHttpViewClose();
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }

    protected boolean checkTargetEnable() {
        if (target instanceof AppCompatActivity) {
            if (((AppCompatActivity) target).isFinishing()) {
                return false;
            }
        } else if (target instanceof Fragment) {
            if (((Fragment) target).isRemoving() || ((Fragment) target).isDetached()) {
                return false;
            }
        }
        return true;
    }

    public abstract void onStart();

    public abstract void onSuccess(T responseEntity);

    public abstract void onFail(T responseEntity, String msg);

    protected abstract void onNextFail(T responseEntity);
}
