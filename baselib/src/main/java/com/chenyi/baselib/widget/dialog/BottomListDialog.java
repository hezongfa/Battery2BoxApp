package com.chenyi.baselib.widget.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenyi.tao.shop.baselib.R;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;

import java.util.ArrayList;

public class BottomListDialog extends BaseDialogFragment {
    public interface BottomListItemClick {
        void onItemClick(int position);
    }

    public int messageGravity = Gravity.CENTER;
    public String title;
    public String message;
    public ArrayList data;
    private BottomListItemClick bottomListItemClick;

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public void setBottomListItemClick(BottomListItemClick bottomListItemClick) {
        this.bottomListItemClick = bottomListItemClick;
    }

    @Override
    protected Dialog dialog() {
        Dialog dialog = getBaseDialog();
        dialog.setContentView(R.layout.dialog_bottom_list);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setWindowAnimations(R.style.BottomDialog);
        window.setAttributes(lp);
        if (StringUtil.isEmpty(title)) {
            dialog.findViewById(R.id.dialog_bottom_list_title).setVisibility(View.GONE);
        }
        ((TextView) dialog.findViewById(R.id.dialog_bottom_list_title)).setText(TextUtils.isEmpty(title) ? "title" : title);
        if (data == null || data.size() == 0) {
            return dialog;
        }
        if (data.size() > 5) {
            LinearLayout.LayoutParams scrollVIewLp = (LinearLayout.LayoutParams) dialog.findViewById(R.id.dialog_bottom_list_scrollview).getLayoutParams();
            scrollVIewLp.height = ViewUtil.dip2px(getContext(), 40 * 5 + 20);
            dialog.findViewById(R.id.dialog_bottom_list_scrollview).setLayoutParams(scrollVIewLp);
        }
        LinearLayout linearLayout = dialog.findViewById(R.id.dialog_bottom_list_layout);
        for (int i = 0; i < data.size(); i++) {
            Object object = data.get(i);
            final int index = i;
//            if (object instanceof View) {
//                //TODO addView 保留，用于直接添加View
//            } else
            if (object instanceof String) {
                TextView textView = new TextView(getContext());
                textView.setText((String) object);
                int height = ViewUtil.dip2px(getContext(), 40);
                textView.setGravity(Gravity.CENTER);
                linearLayout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
//                if (i == data.size() - 1) {
//                    //last
////                    textView.setBackgroundResource(R.drawable.shape_bottom);
//                    textView.setBackgroundColor(Color.parseColor("#ffffff"));
//                } else {
//                    //middle
                textView.setBackgroundColor(Color.parseColor("#ffffff"));
                textView.setTextColor(Color.BLACK);
                View view = new View(getContext());
                view.setBackgroundColor(Color.parseColor("#e6ebed"));
                linearLayout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dip2px(getContext(), 1)));
//                }
//                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.MAIN));
//                TextPaint tp = textView.getPaint();
//                tp.setFakeBoldText(true);
                int finalI = i;
                textView.setOnClickListener(v -> {
                    if (bottomListItemClick != null) {
                        bottomListItemClick.onItemClick(finalI);
                    }
                    dismissAllowingStateLoss();
                });
            }

            dialog.findViewById(R.id.dialog_bottom_list_cancel).setOnClickListener(v -> dismissAllowingStateLoss());
        }
        return dialog;
    }
}
