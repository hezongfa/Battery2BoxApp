package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavigationActivity;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
@Route(path = ARouteHelper.COUPON)
public class CouponActivity extends NavigationActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_4);
    }
}
