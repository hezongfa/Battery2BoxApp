package com.chenyi.baselib.ui;


import android.support.annotation.LayoutRes;

import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;

/**
 * Created by ass on 2018/1/8.
 */

public abstract class HeaderFooterViewModel {
    //数据对象
    public Object object;
    //布局资源id
    public @LayoutRes
    int layoutId;

    public HeaderFooterViewModel(int layoutId, Object object) {
        this.layoutId = layoutId;
        this.object = object;
    }

    public abstract void setData(BaseViewHolder viewHolder, Object object);
}
