package com.chenyi.baselib.ui;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.sys.KeyBoardUtil;
import com.chenyi.tao.shop.baselib.R;


/**
 * Created by ass on 2018/11/28.
 * Description
 */
public abstract class BaseNavActivity extends BaseActivity {
    protected RelativeLayout barLay;
    protected TextView rightTv, titleTv, backTv;
    String navigationTitle;
    TextView emptyTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nav);

        emptyTv = findViewById(R.id.act_nav_empty_tv);
        ViewGroup contentView = findViewById(R.id.act_nav_content_fl);
        getLayoutInflater().inflate(getLayoutId(), contentView);
        contentView.setOnTouchListener((view, motionEvent) -> {
            KeyBoardUtil.hideSoftKeyboard(BaseNavActivity.this);
            return false;
        });
        findViewById(R.id.act_nav_back_tv).setOnClickListener(view -> finish());
        barLay = findViewById(R.id.act_nav_rl);
        titleTv = findViewById(R.id.act_nav_title_tv);
        rightTv = findViewById(R.id.act_nav_right_tv);
        backTv = findViewById(R.id.act_nav_back_tv);
    }

    protected void hideEmptyView() {
        if (emptyTv.getVisibility() == View.VISIBLE) {
            emptyTv.setVisibility(View.INVISIBLE);
        }
    }

    protected void showEmptyView(String text, @DrawableRes int resId) {
        emptyTv.setVisibility(View.VISIBLE);
        if (text != null) {
            emptyTv.setText(text);
        }
        emptyTv.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    //-----bar------start

    protected void setNavigationVisible(boolean visible) {
        barLay.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题
     *
     * @param navigationTitle
     */
    protected void setNavigationTitle(String navigationTitle) {
        this.navigationTitle = navigationTitle;
        titleTv.setText(StringUtil.fixNullStr(this.navigationTitle));
    }

    protected void setNavigationTitle(@StringRes int stringRes) {
        titleTv.setText(stringRes);
    }

    protected void setNavigationBarBackIcon(@DrawableRes int icon) {
        backTv.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
    }

    protected void setNavigationBarBgColor(@ColorInt int color) {
        barLay.setBackgroundColor(color);
    }

    protected void initNavigationRight(String text, View.OnClickListener onClickListener) {
        rightTv.setText(StringUtil.fixNullStr(text));
        rightTv.setOnClickListener(onClickListener);
    }

    protected void setNavigationRightText(String text) {
        rightTv.setText(StringUtil.fixNullStr(text));
    }

}
