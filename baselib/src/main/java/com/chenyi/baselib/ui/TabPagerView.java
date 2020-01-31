package com.chenyi.baselib.ui;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chenyi.baselib.utils.TabLayoutHelper;
import com.chenyi.tao.shop.baselib.R;

import java.util.ArrayList;

/**
 * Created by ass on 2019/2/20.
 * Description
 */
public abstract class TabPagerView extends LinearLayout {
    public TabPagerView(Context context) {
        super(context);
        init(context);
    }

    public TabPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.act_nav_tab_pager, this);
        tabLayout = findViewById(R.id.act_nav_tab_pager_tl_tab);
        tabTitles = getTabTitles();
        viewList = getViewList();
        if (tabTitles == null || viewList == null) {
            return;
        }
        for (int i = 0; i < tabTitles.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab().setText(tabTitles.get(i));
            if (getTipCustomLayout() != 0) {
                tab.setCustomView(getTipCustomLayout());
                setCustomTab(tab.getCustomView(), i,tabTitles.get(i));
            }
            tabLayout.addTab(tab);
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
        viewPager = findViewById(R.id.act_nav_tab_pager_vp);
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
        TabLayoutHelper.setIndicator(tabLayout, 10, 10);
    }

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

    protected void setCurIndex(int index) {
        viewPager.setCurrentItem(index);
    }

    protected int getCurIndex() {
        return viewPager.getCurrentItem();
    }

    protected abstract ArrayList<String> getTabTitles();

    protected abstract ArrayList<NavTabPagerViewI> getViewList();

    protected int getTipCustomLayout() {
        return 0;
    }

    protected void setCustomTab(View customView, int position,String text) {

    }
}
