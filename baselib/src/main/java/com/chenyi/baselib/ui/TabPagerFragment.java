package com.chenyi.baselib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.chenyi.baselib.utils.TabLayoutHelper;
import com.chenyi.tao.shop.baselib.R;

import java.util.ArrayList;

/**
 * Created by ass on 2018/12/21.
 * Description
 */
public abstract class TabPagerFragment extends BaseFragment {
    protected TabLayout tabLayout;
    ArrayList<String> tabTitles;
    ViewPager viewPager;
    VPAdapter vpAdapter;
    protected ArrayList<NavTabPagerViewI> viewList;

    class VPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position).getContentView());
            viewList.get(position).onCreateView(getContext());
            return viewList.get(position).getContentView();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tab_pager;
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void init(Bundle bundle) {
        tabLayout = findViewById(R.id.fragment_tab_pager_tl_tab);
        tabTitles = getTabTitles();
        viewList = getViewList();
        if (tabTitles == null || viewList == null) {
            return;
        }
        for (int i = 0; i < tabTitles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(i)));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager = findViewById(R.id.fragment_tab_pager_vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(vpAdapter = new VPAdapter());
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        TabLayoutHelper.setIndicator(tabLayout, 10, 10);
    }


    protected void setCurIndex(int index) {
        viewPager.setCurrentItem(index);
    }

    public int getCurIndex() {
        return viewPager.getCurrentItem();
    }

    protected abstract ArrayList<String> getTabTitles();

    protected abstract ArrayList<NavTabPagerViewI> getViewList();
}
