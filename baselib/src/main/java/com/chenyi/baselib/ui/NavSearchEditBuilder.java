package com.chenyi.baselib.ui;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.tao.shop.baselib.R;

/**
 * Created by ass on 2019/1/16.
 * Description
 */
public class NavSearchEditBuilder {
    public static EditText build(Context context, int leftDrawableRes, RelativeLayout lay) {
        EditText searchEt = new EditText(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.getDimen(context, R.dimen.x60));
        lp.addRule(RelativeLayout.END_OF, R.id.act_nav_back_tv);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.setMarginStart(ViewUtil.getDimen(context, R.dimen.x10));
        lp.setMarginEnd(ViewUtil.getDimen(context, R.dimen.x32));
        searchEt.setLayoutParams(lp);
        searchEt.setBackgroundResource(R.drawable.bg_search_bar);
        searchEt.setInputType(InputType.TYPE_CLASS_TEXT);
        searchEt.setLines(1);
        searchEt.setGravity(Gravity.CENTER_VERTICAL);
        searchEt.setTextSize(14);
        searchEt.setPadding(ViewUtil.getDimen(context, R.dimen.x20), 0, ViewUtil.getDimen(context, R.dimen.x20), 0);
        searchEt.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawableRes, 0, 0, 0);
        searchEt.setCompoundDrawablePadding(ViewUtil.getDimen(context, R.dimen.x20));
        lay.addView(searchEt);
        searchEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        return searchEt;
    }
}
