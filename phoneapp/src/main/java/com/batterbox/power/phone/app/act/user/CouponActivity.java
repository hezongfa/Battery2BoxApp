package com.batterbox.power.phone.app.act.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavTabPagerActivity;
import com.chenyi.baselib.ui.NavTabPagerViewI;
import com.chenyi.baselib.utils.ViewUtil;

import java.util.ArrayList;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
@Route(path = ARouteHelper.COUPON)
public class CouponActivity extends NavTabPagerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.coupon_1);
        setNavigationVisible(false);
        StatusBarCompat.translucentStatusBar(this, true);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);
        tabLayout.setTabTextColors(Color.parseColor("#4a2f09"), Color.parseColor("#4a2f09"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#e3a64d"));
        tabLayout.setSelectedTabIndicatorHeight(ViewUtil.getDimen(this, R.dimen.x2));
        LinearLayout mainLay = findViewById(R.id.act_nav_tab_pager_ll);
        View v = getLayoutInflater().inflate(R.layout.lay_sw_nav_bar, null);
        mainLay.addView(v, 0);
        v.findViewById(R.id.lay_sw_nav_bar_back_btn).setOnClickListener(v1 -> finish());
        mainLay.addView(getLayoutInflater().inflate(R.layout.lay_coupon_bottom, null));
        findViewById(R.id.lay_coupon_bottom_btn).setOnClickListener(v12 -> ARouteHelper.coupon_get().navigation());
    }

    @Override
    protected ArrayList<String> getTabTitles() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.coupon_3));
        list.add(getString(R.string.coupon_4));
        return list;
    }

    @Override
    protected ArrayList<NavTabPagerViewI> getViewList() {
        ArrayList<NavTabPagerViewI> list = new ArrayList<>();
        list.add(new CouponListView(this, 1));
        list.add(new CouponListView(this, 0));
        return list;
    }


}
