package com.chenyi.baselib.widget.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.chenyi.baselib.utils.http.IHttpView;
import com.chenyi.baselib.utils.print.FQL;

/**
 * Created by ass on 2018/3/21.
 */

public class NormalLoadingDialog extends LoadingDialog implements IHttpView {

    @Override
    public void onIHttpViewShow(Object target) {
        if (target != null) {
            if (target instanceof Fragment && ((Fragment) target).getContext() != null && !((Fragment) target).isRemoving()) {
                show(((Fragment) target).getFragmentManager());
            } else if (target instanceof FragmentActivity && !((FragmentActivity) target).isFinishing()) {
                show(((FragmentActivity) target).getSupportFragmentManager());
            }
        }
    }

    @Override
    public void onIHttpViewClose() {
        if (getContext() == null) {
            FQL.d("onIHttpViewClose", "getContext");
            return;
        }
        if (isRemoving()) {
            FQL.d("onIHttpViewClose", "isRemoving");
            return;
        }
        if (isDetached()) {
            FQL.d("onIHttpViewClose", "isDetached");
            return;
        }
        if (getActivity() == null) {
            FQL.d("onIHttpViewClose", "getActivity  null");
            return;
        }
        FQL.d("onIHttpViewClose", "getActivity()==" + getActivity().getClass().getName());
        if (getActivity() != null && getActivity().isFinishing()) {
            FQL.d("onIHttpViewClose", "isDetached");
            return;
        }
        if (getActivity() != null && getActivity().isDestroyed()) {
            FQL.d("onIHttpViewClose", "isDetached");
            return;
        }
        FQL.d("onIHttpViewClose", "dismissAllowingStateLoss()");
        dismissAllowingStateLoss();
    }

    @Override
    public boolean isViewCanceled() {
        if (isRemoving() || isDetached() || getDialog() == null
                || !getDialog().isShowing()) {
            return true;
        }
        return false;
    }

}
