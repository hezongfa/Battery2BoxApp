package com.chenyi.baselib.widget.wheelview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.tao.shop.baselib.R;


/**
 * Created by ZongfaHe on 16/3/15.
 */
public class NumPickerDialog extends Dialog {
    View view;
    WheelView numWheel;
    int num = 1;

    public NumPickerDialog(Context context) {
        super(context, R.style.dialog_picker);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_num_picker, null);
        view.setMinimumWidth(ViewUtil.getScreenDM((Activity) context).widthPixels);
        Window window = getWindow();
        window.setContentView(view);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        numWheel = (WheelView) view.findViewById(R.id.num);
        numWheel.setAdapter(new NumericWheelAdapter(1, 100000));
        numWheel.addChangingListener((wheel, oldValue, newValue) -> num = newValue + 1);

        view.findViewById(R.id.btnCancle).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnOk).setOnClickListener(v -> {
            if (mSelectListener != null) {
                mSelectListener.onSelect(num);
            }
            dismiss();
        });
    }

    SelectListener mSelectListener;

    public void setSelectListener(SelectListener selectListener) {
        mSelectListener = selectListener;
    }

    public interface SelectListener {
        void onSelect(int num);
    }
}
