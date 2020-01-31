package com.chenyi.baselib.ui;

import android.content.Context;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

class HeaderFooterAdapter<G> extends QuickDelegateAdapter<G> {
        HeaderFooterViewModel model;
        int viewType;

        public HeaderFooterAdapter(Context context, int viewType, int layoutResId, HeaderFooterViewModel footerViewModel, ArrayList list) {
            super(context, layoutResId, list);
            this.model = footerViewModel;
            this.viewType = viewType;
        }

        @Override
        protected void onSetItemData(BaseViewHolder holder, G item, int viewType) {
            model.setData(holder, item);
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper(1);
        }

        @Override
        public int getItemViewType(int position) {
            return viewType;
        }
    }