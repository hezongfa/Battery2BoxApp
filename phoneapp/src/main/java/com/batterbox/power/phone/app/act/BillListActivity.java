package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.BillEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.widget.ItemSlideHelper;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-23.
 * Description
 */
@Route(path = ARouteHelper.USER_BILL)
public class BillListActivity extends NavListActivity<BillEntity> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.wallet_1);
        recyclerView.addOnItemTouchListener(new ItemSlideHelper(BillListActivity.this, new ItemSlideHelper.Callback() {
            @Override
            public int getHorizontalRange(RecyclerView.ViewHolder holder) {
                return ViewUtil.getDimen(BillListActivity.this, R.dimen.x120);
            }

            @Override
            public RecyclerView.ViewHolder getChildViewHolder(View childView) {
                if (childView == null)
                    return null;

                return recyclerView.getChildViewHolder(childView);
            }

            @Override
            public View findTargetView(float x, float y) {
                return recyclerView.findChildViewUnder(x, y);
            }

            @Override
            public boolean checkTouchRule(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public int getSpanCount() {
                return 1;
            }

            @Override
            public void onSlideOpen() {

            }
        }, ViewUtil.getDimen(BillListActivity.this, R.dimen.x120)));
    }

    @Override
    protected QuickDelegateAdapter<BillEntity> getAdapter() {
        return new QuickDelegateAdapter<BillEntity>(this, R.layout.item_bill) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, BillEntity item, int viewType) {
                holder.setText(R.id.item_bill_title_order_tv, StringUtil.fixNullStr(item.payCode));
                holder.setText(R.id.item_bill_title_tv, StringUtil.fixNullStr(item.transactionName));
                holder.setText(R.id.item_bill_time_tv, StringUtil.fixNullStr(item.transactionTime));
                if (StringUtil.isEquals(item.mwdType, "1")) {
                    holder.setText(R.id.item_bill_money_tv, "+" + CnyUtil.getPriceByUnit(context, item.changeBalance));
                } else if (StringUtil.isEquals(item.mwdType, "2")) {
                    holder.setText(R.id.item_bill_money_tv, "-" + CnyUtil.getPriceByUnit(context, item.changeBalance));
                }
                holder.setOnClickListener(R.id.item_bill_delete_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().us_billDetail(page, pageSize, new NormalHttpCallBack<ResponseEntity<ArrayList<BillEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<BillEntity>> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    handleListData(responseEntity.getData());
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<BillEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }
}
