package com.batterbox.power.phone.app.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.recycleviewadapter.BaseViewHolder;
import com.chenyi.baselib.widget.recycleviewadapter.QuickRecycleAdapter;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.List;

/**
 * Created by ass on 2020-05-04.
 * Description
 */
public class PopUtil {
    public static class PopItem {
        public String title;
        public int res;

        public PopItem(String title, int res) {
            this.title = title;
            this.res = res;
        }

        public PopItem(String title) {
            this.title = title;
        }
    }

    public interface OnMenuPopItemSelectListener {
        void onSelect(PopItem popItem, int position);
    }

    public static void showMenuPop(Context context, View mAttachView, List<PopItem> data, OnMenuPopItemSelectListener onMenuPopItemSelectListener) {
        PopupWindow mMenuWindow = new PopupWindow(context);
        View menuView = LayoutInflater.from(context).inflate(R.layout.pop_main_chat, null);
        // 设置布局文件
        mMenuWindow.setContentView(menuView);
        mMenuWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent));
//        mMenuWindow.setWidth(ScreenUtil.getPxByDp(160));
//        mMenuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        mMenuWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_pop));
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mMenuWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mMenuWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mMenuWindow.setOutsideTouchable(true);
//        backgroundAlpha(0.5f);
        // 相对于 + 号正下面，同时可以设置偏移量
        mMenuWindow.showAtLocation(mAttachView, Gravity.END | Gravity.TOP, 0, ScreenUtil.getPxByDp(64));
        // 设置pop关闭监听，用于改变背景透明度
        mMenuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                backgroundAlpha(1.0f);
            }
        });

        if (StringUtil.isEmpty(data)) return;
        RecyclerView rv = menuView.findViewById(R.id.pop_main_chat_rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new QuickRecycleAdapter<PopItem>(context, R.layout.item_pop_main_chat, data) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, PopItem item, int viewType) {
                holder.setText(R.id.item_pop_main_chat_tv, StringUtil.fixNullStr(item.title));
                ((TextView) holder.getView(R.id.item_pop_main_chat_tv)).setCompoundDrawablesRelativeWithIntrinsicBounds(item.res, 0, 0, 0);
                holder.setVisible(R.id.item_pop_main_chat_iv, holder.getAdapterPosition() != getItemCount() - 1);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onMenuPopItemSelectListener != null) {
                            onMenuPopItemSelectListener.onSelect(item, holder.getAdapterPosition());
                        }
                        mMenuWindow.dismiss();
                    }
                });
            }
        });
    }

}
