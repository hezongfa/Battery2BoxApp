package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.AreaCodeDataEntity;
import com.batterbox.power.phone.app.entity.AreaCodeEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.HeaderFooterViewModel;
import com.chenyi.baselib.ui.SearchListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.recycleviewadapter.QuickRecycleAdapter;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-09.
 * Description
 */
@Route(path = ARouteHelper.AREA_CODE)
public class AreaCodeActivity extends SearchListActivity<AreaCodeEntity> {
    AreaCodeDataEntity areaCodeDataEntity;

    @Override
    protected HeaderFooterViewModel getHeaderView() {
        return new HeaderFooterViewModel(R.layout.lay_area_code_head, null) {
            @Override
            public void setData(BaseViewHolder viewHolder, Object object) {
                if (areaCodeDataEntity == null) {
                    viewHolder.setVisible(R.id.lay_area_code_head_hot_tv, false);
                    viewHolder.setVisible(R.id.lay_area_code_head_rv, false);
                } else {
                    viewHolder.setVisible(R.id.lay_area_code_head_hot_tv, true);
                    viewHolder.setVisible(R.id.lay_area_code_head_rv, true);
                    RecyclerView rv = viewHolder.getView(R.id.lay_area_code_head_rv);
                    rv.setLayoutManager(new GridLayoutManager(AreaCodeActivity.this, 4));
                    if (!StringUtil.isEmpty(areaCodeDataEntity.usualMobilPrefix)) {
                        rv.setAdapter(new QuickRecycleAdapter<AreaCodeEntity>(AreaCodeActivity.this, R.layout.item_area_code_hot, areaCodeDataEntity.usualMobilPrefix) {
                            @Override
                            protected void onSetItemData(com.chenyi.baselib.widget.recycleviewadapter.BaseViewHolder holder, AreaCodeEntity item, int viewType) {
                                holder.setText(R.id.item_area_code_hot_tv, StringUtil.fixNullStr(item.country));
                                holder.itemView.setOnClickListener(v -> selectArea(item));
                            }
                        });
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadMoreEnable(false);
        setRefreshEnable(false);
        searchEt.setHint(R.string.login_22);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fill(s.toString());
            }
        });
        getData();
    }

    private void selectArea(AreaCodeEntity areaCodeEntity) {
        Intent intent = new Intent();
        intent.putExtra("areaCodeEntity", areaCodeEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected QuickDelegateAdapter<AreaCodeEntity> getAdapter() {
        return new QuickDelegateAdapter<AreaCodeEntity>(this, R.layout.item_area_code) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, AreaCodeEntity item, int viewType) {
                holder.setText(R.id.item_area_code_country_tv, StringUtil.fixNullStr(item.country));
                holder.setText(R.id.item_area_code_value_tv, "+" + StringUtil.fixNullStr(item.mobilePrefix));
                holder.itemView.setOnClickListener(v -> selectArea(item));
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        fill(searchEt.getText().toString());
    }

    private void fill(String content) {
        if (areaCodeDataEntity != null && !StringUtil.isEmpty(areaCodeDataEntity.allMobilPrefix)) {
            if (StringUtil.isEmpty(content)) {
                adapter.replaceAll(areaCodeDataEntity.allMobilPrefix);
                return;
            }
            ArrayList<AreaCodeEntity> list = new ArrayList<>();
            for (AreaCodeEntity areaCodeEntity : areaCodeDataEntity.allMobilPrefix) {
                if (!StringUtil.isEmpty(areaCodeEntity.country) && areaCodeEntity.country.contains(content)) {
                    list.add(areaCodeEntity);
                }
            }
            adapter.replaceAll(list);
        }
    }

    private void getData() {
        HttpClient.getInstance().openApi_countryPrefix(new NormalHttpCallBack<AreaCodeDataEntity>((BaseActivity) this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(AreaCodeDataEntity responseEntity) {
                if (responseEntity != null) {
                    areaCodeDataEntity = responseEntity;
                    if (!StringUtil.isEmpty(responseEntity.allMobilPrefix)) {
                        adapter.replaceAll(responseEntity.allMobilPrefix);
                    }
                    delegateAdapter.notifyItemChanged(0);
                }
            }

            @Override
            public void onFail(AreaCodeDataEntity responseEntity, String msg) {
                FQT.showShort(AreaCodeActivity.this, msg);
            }
        });
    }
}
