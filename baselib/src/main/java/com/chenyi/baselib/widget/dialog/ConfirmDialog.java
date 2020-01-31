package com.chenyi.baselib.widget.dialog;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.autofixtext.AutofitTextView;
import com.chenyi.tao.shop.baselib.R;


/**
 * Created by ass on 2017/1/2.
 */

public class ConfirmDialog extends BaseDialogFragment {
    AutofitTextView mTitleTv, mContentTv;
    Button mOneBtn, mTwoBtn;
    public String mTitle, mContent, mOneStr, mTwoStr;
    public View.OnClickListener mOneBtnClick, mTwoBtnClick;

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), getDialog().getWindow().getAttributes().height);
    }

    @Override
    protected Dialog dialog() {
//        int confirmColor = Color.parseColor("#080a2b");
        if (getContext() == null) return null;
//        int confirmColor = ContextCompat.getColor(getContext(), R.color.red_d72c2a);
        Dialog dialog = getBaseDialog();
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null, false);
        mTitleTv = v.findViewById(R.id.ConfirmDialog_mTitleTv);
        mContentTv = v.findViewById(R.id.ConfirmDialog_mContentTv);
        mOneBtn = v.findViewById(R.id.ConfirmDialog_mOneBtn);
        mTwoBtn = v.findViewById(R.id.ConfirmDialog_mTwoBtn);
        if (StringUtil.isEmpty(mTitle)) {
            mTitleTv.setVisibility(View.GONE);
        } else {
            mTitleTv.setText(StringUtil.fixNullStr(mTitle, "提示"));
        }
        mContentTv.setText(StringUtil.fixNullStr(mContent, ""));


//        if (mOneBtnClick != null) {
//            if (mTwoBtnClick != null) {
////                mTwoBtn.setVisibility(View.VISIBLE);
////                mOneBtn.setVisibility(View.VISIBLE);
//            } else {
//                mOneBtn.setVisibility(View.GONE);
//            }
//        } else {
//            mOneBtn.setVisibility(View.GONE);
//        }
        if (mOneStr != null && mTwoStr != null) {
            mOneBtn.setText(StringUtil.fixNullStr(mOneStr, "取消"));
            mTwoBtn.setText(StringUtil.fixNullStr(mTwoStr, "确定"));
            mOneBtn.setOnClickListener(v1 -> {
                dismiss();
                if (mOneBtnClick != null) mOneBtnClick.onClick(v1);
            });
            mTwoBtn.setOnClickListener(v2 -> {
                dismiss();
                if (mTwoBtnClick != null) mTwoBtnClick.onClick(v2);
            });

        } else if (mOneStr != null || mTwoStr == null) {
            mTwoBtn.setText(StringUtil.fixNullStr(mOneStr, "确定"));
            mOneBtn.setVisibility(View.GONE);
            mTwoBtn.setOnClickListener(v2 -> {
                dismiss();
                if (mOneBtnClick != null) mOneBtnClick.onClick(v2);
            });
        }

        dialog.setTitle(null);
        dialog.setContentView(v);
        dialog.show();
        return dialog;
    }


}
