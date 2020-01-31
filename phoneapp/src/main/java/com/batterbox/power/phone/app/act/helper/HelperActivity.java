package com.batterbox.power.phone.app.act.helper;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavTabPagerActivity;
import com.chenyi.baselib.ui.NavTabPagerViewI;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-08.
 * Description
 */
@Route(path = ARouteHelper.HELPER_DETAIL)
public class HelperActivity extends NavTabPagerActivity {
    @Autowired
    public String lat;
    @Autowired
    public String lng;
    HelperAdviceView helperAdviceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_8);
        findViewById(R.id.act_nav_tab_pager_ll).setBackgroundResource(R.mipmap.pic_bg);
    }

    @Override
    protected ArrayList<String> getTabTitles() {
        ArrayList<String> tabTitles = new ArrayList<>();
        tabTitles.add(getString(R.string.helper_1));
        tabTitles.add(getString(R.string.helper_2));
        return tabTitles;
    }

    @Override
    protected ArrayList<NavTabPagerViewI> getViewList() {
        ArrayList<NavTabPagerViewI> viewIS = new ArrayList<>();
        viewIS.add(helperAdviceView = new HelperAdviceView(this));
        viewIS.add(new HelperProblemsView(this));
        return viewIS;
    }


}
