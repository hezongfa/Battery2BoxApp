package com.chenyi.baselib.widget.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ass on 2017/1/2.
 */

public class DialogUtils {
    public static LoadingDialog showLoading(FragmentManager fm) {
        if (fm == null) return null;
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(fm);
        return loadingDialog;
    }

    public static LoadingDialog showLoading(FragmentManager fm, String text) {
        if (fm == null) return null;
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.setLoadingText(text);
        loadingDialog.show(fm);
        return loadingDialog;
    }


    public static ConfirmDialog showTipDialog(FragmentManager fm, String content) {
        return showDialog(fm, "", content, "确定", null);
    }

    public static ConfirmDialog showDialog(FragmentManager fm, String title, String content) {
        return showDialog(fm, title, content, "确定", null);
    }

    public static ConfirmDialog showDialog(FragmentManager fm, String title, String content, String okStr) {
        return showDialog(fm, title, content, okStr, null);
    }

    public static ConfirmDialog showDialog(FragmentManager fm, String title, String content, String oneStr, View.OnClickListener oneClick) {
        return showDialog(fm, title, content, oneStr, oneClick, null, null);
    }

    public static ConfirmDialog showDialog(FragmentManager fm, String title, String content, String oneStr, View.OnClickListener oneClick, String twoStr, View.OnClickListener twoClick) {
        if (fm == null) return null;
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.mTitle = title;
        confirmDialog.mContent = content;
        confirmDialog.mOneStr = oneStr;
        confirmDialog.mTwoStr = twoStr;
        confirmDialog.mOneBtnClick = oneClick;
        confirmDialog.mTwoBtnClick = twoClick;
        confirmDialog.show(fm, "ConfirmDialog");
        return confirmDialog;
    }

    /**
     * 底部List选择Dialog
     *
     * @param context
     * @return
     */
    public static BaseDialogFragment showListDialog(Context context, String title, ArrayList<String> data, BottomListDialog.BottomListItemClick bottomListItemClick) {
        if (!(context instanceof FragmentActivity)) return null;
        FragmentActivity activity = (FragmentActivity) context;
        BottomListDialog dialogFragment = new BottomListDialog();
        dialogFragment.title = title;
        dialogFragment.data = data;
        dialogFragment.setBottomListItemClick(bottomListItemClick);
        dialogFragment.show(activity.getSupportFragmentManager(), "AlertDialog");
        return dialogFragment;
    }
}
