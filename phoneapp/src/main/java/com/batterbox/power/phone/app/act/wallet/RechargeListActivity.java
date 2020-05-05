package com.batterbox.power.phone.app.act.wallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.RechargeEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.baselib.widget.recycleviewadapter.BaseViewHolder;
import com.chenyi.baselib.widget.recycleviewadapter.QuickRecycleAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-09-04.
 * Description
 */

@Route(path = ARouteHelper.RECHARGE_LIST)
public class RechargeListActivity extends NavigationActivity {
    RecyclerView rv;
    QuickRecycleAdapter<RechargeEntity> adapter;
    RechargeEntity selectRechargeEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_recharge_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.wallet_6);
//        setNavigationBarBgColor(Color.parseColor("#f1bf39"));
//        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#f1bf39"));
        rv = findViewById(R.id.act_recharge_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter = new QuickRecycleAdapter<RechargeEntity>(this, R.layout.item_recharge_list) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, RechargeEntity item, int viewType) {
                holder.setText(R.id.item_recharge_list_tv, CnyUtil.getPriceByUnit(context, item.money));
                holder.setText(R.id.item_recharge_list_tv2, "+" + StringUtil.fixNullStr(item.giveTime) + getString(R.string.wallet_28));
                holder.itemView.setOnClickListener(v -> {
                    selectRechargeEntity = item;
                    notifyDataSetChanged();
                });

                if (item.equals(selectRechargeEntity)) {
                    holder.setTextColor(R.id.item_recharge_list_tv, Color.parseColor("#FFFE9D00"));
                    holder.setTextColor(R.id.item_recharge_list_tv2, Color.parseColor("#FFFE9D00"));
                    holder.setTextColor(R.id.item_recharge_list_tv3, Color.parseColor("#FFFE9D00"));
                    holder.setBackgroundRes(R.id.item_recharge_list_ll, R.drawable.bg_ffffff_r10px_selected);
                } else {
                    holder.setTextColor(R.id.item_recharge_list_tv, Color.parseColor("#000000"));
                    holder.setTextColor(R.id.item_recharge_list_tv2, Color.parseColor("#FF717171"));
                    holder.setTextColor(R.id.item_recharge_list_tv3, Color.parseColor("#FF717171"));
                    holder.setBackgroundRes(R.id.item_recharge_list_ll, R.drawable.bg_ffffff_r10px);
                }
            }
        });
        findViewById(R.id.act_recharge_list_submit_btn).setOnClickListener(v -> {
            if (selectRechargeEntity == null) return;
            DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.wallet_30), getString(R.string.app_16), v12 -> {

            }, getString(R.string.app_11), v1 -> HttpClient.getInstance().recharge_getRechargeTable(selectRechargeEntity.rcId, new NormalHttpCallBack<ResponseEntity>(RechargeListActivity.this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity responseEntity) {
                    DialogUtils.showTipDialog(getSupportFragmentManager(), StringUtil.fixNullStr(responseEntity.getMsg()));
                }

                @Override
                public void onFail(ResponseEntity responseEntity, String msg) {
                    FQT.showShort(RechargeListActivity.this, msg);
                    if(responseEntity.getCode()==10117){
                        ARouteHelper.wallet_card_edit().navigation();
                    }
                }
            }));

        });
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ((TextView) findViewById(R.id.act_recharge_list_price_tv)).setText(getString(R.string.wallet_6) + ":" + CnyUtil.getPriceByUnit(this, userEntity.balance));
        }
        HttpClient.getInstance().recharge_listCard(new NormalHttpCallBack<ResponseEntity<ArrayList<RechargeEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<RechargeEntity>> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    if (responseEntity.getData().size() > 0) {
                        selectRechargeEntity = responseEntity.getData().get(0);
                    }
                    adapter.replaceAll(responseEntity.getData());
                }
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<RechargeEntity>> responseEntity, String msg) {
                FQT.showShort(RechargeListActivity.this, msg);
            }
        });
    }
}
