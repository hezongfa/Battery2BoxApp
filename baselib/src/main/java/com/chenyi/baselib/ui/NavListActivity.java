package com.chenyi.baselib.ui;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.chenyi.baselib.utils.http.IHttpView;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;
import com.chenyi.tao.shop.baselib.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class NavListActivity<T> extends NavigationActivity implements IHttpView {
    protected DelegateAdapter delegateAdapter;
    protected int page = 1;
    protected int pageSize = 20;
    protected SmartRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    protected QuickDelegateAdapter<T> adapter;
    HeaderFooterViewModel headerViewModel, footerViewModel;
    protected LinearLayout topLay, bottomLay;
    protected LinearLayout mainLay;

    protected VirtualLayoutManager layoutManager;

    @Override
    public void onIHttpViewShow(Object target) {

    }

    @Override
    public void onIHttpViewClose() {
        stopRefresh();
    }

    @Override
    public boolean isViewCanceled() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLay = findViewById(R.id.act_nav_list_mainLay);
        topLay = findViewById(R.id.act_nav_list_topLay);
        bottomLay = findViewById(R.id.act_nav_list_bottomLay);
        recyclerView = findViewById(R.id.act_nav_list_rv);
        refreshLayout = findViewById(R.id.act_nav_list_refreshLayout);
        refreshLayout.setOnRefreshListener(refreshLayout -> refresh());
        refreshLayout.setOnLoadmoreListener(refreshLayout -> loadMore());
        if (getTopLayId() != 0) {
            getLayoutInflater().inflate(getTopLayId(), topLay, true);
        }
        if (getBottomLayId() != 0) {
            getLayoutInflater().inflate(getBottomLayId(), bottomLay, true);
        }
        initRv();
    }

    protected void setMainBg(int res) {
        mainLay.setBackgroundResource(res);
    }

    protected int getTopLayId() {
        return 0;
    }

    protected int getBottomLayId() {
        return 0;
    }

    protected void initRv() {
        layoutManager = new VirtualLayoutManager(this);

//        layoutManager.setRecycleOffset(300);

        recyclerView.setLayoutManager(layoutManager);
        // layoutManager.setReverseLayout(true);

//        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = ((VirtualLayoutManager.LayoutParams) view.getLayoutParams()).getViewPosition();
//                outRect.set(4, 4, 4, 4);
//            }
//        };


        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        recyclerView.setRecycledViewPool(viewPool);

        // recyclerView.addItemDecoration(itemDecoration);

        viewPool.setMaxRecycledViews(0, 28);

        delegateAdapter = new DelegateAdapter(layoutManager, true);

        recyclerView.setAdapter(delegateAdapter);

        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        headerViewModel = getHeaderView();
        if (headerViewModel != null) adapters.add(getHeadViewAdapter());

        adapters.add(adapter = getAdapter());

        footerViewModel = getFooterView();
        if (footerViewModel != null) adapters.add(getFooterViewAdapter());

        delegateAdapter.setAdapters(adapters);
    }

    protected abstract QuickDelegateAdapter getAdapter();

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        refreshLayout.autoRefresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_nav_list;
    }

    protected boolean isRefreshFinish() {
        return refreshLayout.getState() == RefreshState.None;
    }

    protected void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        } else if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadmore();
        }
    }

    public void checkEmpty() {
        checkEmpty(null, R.mipmap.pic_none_data);
    }

    public void checkEmpty(String text, @DrawableRes int resId) {
        if (adapter.getItemCount() == 0) {
            showEmptyView(text, resId);
        } else {
            hideEmptyView();
        }
    }

    protected void refresh() {
        hideEmptyView();
        page = 1;
        adapter.clear();
        getData(page, pageSize);
    }

    protected void loadMore() {
        page++;
        getData(page, pageSize);
    }

    protected void setRefreshEnable(boolean enable) {
        refreshLayout.setEnableRefresh(enable);
//        if (!enable) {
//            setLoadMoreEnable(false);
//        }
    }

    protected void setLoadMoreEnable(boolean enable) {
        refreshLayout.setEnableLoadmore(enable);
    }

    /**
     * 单一列表数据通用处理数据方法，getData方法获取到数据后调用
     *
     * @param data
     * @param
     */
    protected void handleListData(List<T> data) {
        handleListData(data, data == null ? 0 : data.size());
    }

    protected void handleListData(List<T> data, int realDataSize) {
        stopRefresh();
        if (data != null) {
            if (page == 1) {
                adapter.replaceAll(data);
            } else {
                adapter.addAll(data);
            }
            if (realDataSize < pageSize) {
                setLoadMoreEnable(false);
            } else {
                setLoadMoreEnable(true);
            }
        } else {
            setLoadMoreEnable(false);
        }
    }

    protected abstract void getData(int page, int pageSize);

    protected HeaderFooterViewModel getHeaderView() {
        return null;
    }

    protected HeaderFooterViewModel getFooterView() {
        return null;
    }

    protected DelegateAdapter.Adapter getHeadViewAdapter() {
        ArrayList list = new ArrayList<>();
        list.add(headerViewModel.object);
        return new HeaderFooterAdapter(this, 119, headerViewModel.layoutId, headerViewModel, list);
    }

    private DelegateAdapter.Adapter getFooterViewAdapter() {
        ArrayList list = new ArrayList<>();
        list.add(footerViewModel.object);
        return new HeaderFooterAdapter(this, 120, footerViewModel.layoutId, footerViewModel, list);
    }


}
