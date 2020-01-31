package com.chenyi.baselib.widget.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.tao.shop.baselib.R;

/**
 * LoadingDialog.java
 *
 * @author zongfa_he
 * @description 加载中view
 * @since 2014-7-15 下午7:41:05
 */
public class LoadingDialog extends BaseDialogFragment {
    String contentStr;
    TextView loading_content;
    ImageView image;
    //    AVLoadingIndicatorView progress_wheel;
    ProgressBar progress_wheel;

    @Override
    protected Dialog dialog() {
        Dialog m_dialog = getBaseDialog(R.style.loading_dialog);
        m_dialog.setCancelable(false);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null, false);
        loading_content = v.findViewById(R.id.dialog_text);
        image = v.findViewById(R.id.image);
        progress_wheel = v.findViewById(R.id.progress_wheel);
        m_dialog.setTitle(null);
        m_dialog.setContentView(v);
        loading_content.setText(StringUtil.fixNullStr(contentStr));
        if (contentStr != null) {
            loading_content.setVisibility(View.VISIBLE);
        }
        return m_dialog;
    }


    public void setLoadingText(String string) {
        if (loading_content == null) return;
        contentStr = string;
        if (contentStr != null) {
            loading_content.setVisibility(View.VISIBLE);
            loading_content.setText(string);
        } else {
            loading_content.setVisibility(View.GONE);
        }


    }

    public void setLoadingText(String string, boolean isSuccess) {
        setLoadingText(string);
        setStatue(isSuccess);
    }

    public void setStatue(boolean isSuccess) {
        progress_wheel.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        if (isSuccess) {
//            image.setImageResource(R.mipmap.pic_success);
        } else {
//            image.setImageResource(R.mipmap.pic_error);
        }
    }

    public void show(FragmentManager manager) {
        show(manager, getClass().getName());
    }
//
//    @Override
//    public void showV(Object target) {
//        if (target != null) {
//            if (target instanceof Fragment && ((Fragment) target).getContext() != null && !((Fragment) target).isRemoving()) {
//                show(((Fragment) target).getFragmentManager());
//            } else if (target instanceof FragmentActivity && !((Activity) target).isFinishing()) {
//                show(((FragmentActivity) target).getSupportFragmentManager());
//            }
//        }
//    }
//
//
//    @Override
//    public void dismissV(Object target) {
//        if (target != null) {
//            if (target instanceof Fragment && ((Fragment) target).getContext() != null && !((Fragment) target).isRemoving()) {
//                dismiss();
//            } else if (target instanceof AppCompatActivity && !((Activity) target).isFinishing()) {
//                dismiss();
//            }
//        }
//    }

}
