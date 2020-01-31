package com.chenyi.baselib.widget.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.chenyi.tao.shop.baselib.R;

/**
 * LoadingDialog.java
 *
 * @author zongfa_he
 * @description 加载中view
 * @since 2014-7-15 下午7:41:05
 */
public class RotateLoadingDialog extends BaseDialogFragment {

    @Override
    protected Dialog dialog() {
        Dialog m_dialog = getBaseDialog(R.style.rotate_loading_dialog);
        m_dialog.setCancelable(true);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_rotate, null, false);
        m_dialog.setTitle(null);
        m_dialog.setContentView(v);
        return m_dialog;
    }


    public void show(FragmentManager manager) {
        show(manager, getClass().getName());
    }

}
