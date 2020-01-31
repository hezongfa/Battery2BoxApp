package com.chenyi.baselib.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.chenyi.tao.shop.baselib.R;

import java.lang.reflect.Field;

public abstract class BaseDialogFragment extends DialogFragment {

    protected abstract Dialog dialog();

    DialogInterface.OnDismissListener onDismissListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = dialog();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (onDismissListener != null) onDismissListener.onDismiss(dialog1);
//        });
        return dialog;
    }

    public BaseDialogFragment setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        // 如果使用api本身自带的 show() 方法，调用的是 commit() 方法添加 DialogFragment，这样的话会导致
        // 当 Activity 调用 onSaveInstanceState() 方法后加载弹出框时报 IllegalStateException 异常，
        // 所以需要通过反射重写 show() 方法，使用 commitAllowingStateLoss() 方法添加 DialogFragment.
        try {
            Class<?> classDialogFragment = getClass().getSuperclass();
            while (!classDialogFragment.equals(DialogFragment.class)) {
                classDialogFragment = classDialogFragment.getSuperclass();
            }
            Field mDismissed = classDialogFragment.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);
            Field mShownByMe = classDialogFragment.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, false);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (getDialog() != null) {
            super.dismissAllowingStateLoss();
        }
    }

    protected Dialog getBaseDialog() {
        return getBaseDialog(0);
    }

    protected Dialog getBaseDialog(int style) {
        Dialog dialog;
        if (style == 0) {
            dialog = new Dialog(getActivity(), R.style.BaseDialogFragment);
        }else{
            dialog = new Dialog(getActivity(), style);}
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
            if (window != null) {window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);}
        }

        return dialog;
    }
}
