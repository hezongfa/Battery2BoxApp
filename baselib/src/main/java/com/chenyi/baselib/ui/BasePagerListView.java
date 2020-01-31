package com.chenyi.baselib.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.http.IHttpView;
import com.chenyi.baselib.widget.PageRecyclerView;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;
import com.chenyi.tao.shop.baselib.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ass on 2018/3/14.
 */

public abstract class BasePagerListView<T> extends RelativeLayout implements IHttpView {
    public boolean isInited = false;
    protected int page = 1;
    protected int pageSize = 20;
    protected SmartRefreshLayout refreshLayout;
    protected PageRecyclerView recyclerView;
    protected QuickDelegateAdapter<T> adapter;
    HeaderFooterViewModel headerViewModel, footerViewModel;
    protected LinearLayout topLay, bottomLay;
    protected TextView emptyTv;

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

    public BasePagerListView(Context context) {
        super(context);
        init(context);
    }

    public BasePagerListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_base_pager_list_view, this);
        emptyTv = findViewById(R.id.view_base_pager_list_view_empty_tv);
        topLay = findViewById(R.id.view_base_pager_list_view_topLay);
        bottomLay = findViewById(R.id.view_base_pager_list_view_bottomLay);
        recyclerView = findViewById(R.id.view_base_pager_list_view_rv);
        refreshLayout = findViewById(R.id.view_base_pager_list_view_refreshLayout);
        refreshLayout.setOnRefreshListener(refreshLayout -> refresh());
        refreshLayout.setOnLoadmoreListener(refreshLayout -> loadMore());
        if (getTopLayId() != 0) {
            layoutInflater.inflate(getTopLayId(), topLay, true);
        }
        if (getBottomLayId() != 0) {
            layoutInflater.inflate(getBottomLayId(), bottomLay, true);
        }
        initRv();
        postDelayed(() -> refreshLayout.autoRefresh(), 100);
        isInited = true;
    }

    protected int getTopLayId() {
        return 0;
    }

    protected int getBottomLayId() {
        return 0;
    }

    protected void initRv() {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getContext());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            }
        });

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

        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);

        recyclerView.setAdapter(delegateAdapter);

        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        headerViewModel = getHeaderView();
        if (headerViewModel != null) adapters.add(getHeadViewAdapter());

        adapters.add(adapter = getAdapter());

        footerViewModel = getFooterView();
        if (footerViewModel != null) adapters.add(getFooterViewAdapter());

        delegateAdapter.setAdapters(adapters);
    }

    protected void showEmptyView(String text, @DrawableRes int resId) {
//        refreshLayout.setVisibility(View.INVISIBLE);
        emptyTv.setVisibility(View.VISIBLE);
        if (text != null) {
            emptyTv.setText(text);
        }
        emptyTv.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
    }

    protected void hideEmptyView() {
        if (emptyTv.getVisibility() == View.VISIBLE) {
            emptyTv.setVisibility(View.INVISIBLE);
//            refreshLayout.setVisibility(View.VISIBLE);
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

    protected abstract QuickDelegateAdapter getAdapter();


    protected void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        } else if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadmore();
        }
    }

    public void refresh() {
        adapter.clear();
        page = 1;
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
     */
    protected void handleListData(List<T> data) {
        handleListData(data, data.size());
    }

    protected void handleListData(List<T> data, int realDataSize) {
        stopRefresh();
        if (!StringUtil.isEmpty(data)) {
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
        return new HeaderFooterAdapter(BasePagerListView.this.getContext(), 119, headerViewModel.layoutId, headerViewModel, list);
    }

    private DelegateAdapter.Adapter getFooterViewAdapter() {
        ArrayList list = new ArrayList<>();
        list.add(footerViewModel.object);
        return new HeaderFooterAdapter(BasePagerListView.this.getContext(), 120, footerViewModel.layoutId, footerViewModel, list);
    }


    class HeaderFooterAdapter<G> extends QuickDelegateAdapter<G> {
        HeaderFooterViewModel model;
        int viewType;

        private HeaderFooterAdapter(Context context, int viewType, int layoutResId, HeaderFooterViewModel footerViewModel, ArrayList list) {
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

}
