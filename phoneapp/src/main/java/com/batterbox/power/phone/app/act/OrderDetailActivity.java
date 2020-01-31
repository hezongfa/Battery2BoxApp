package com.batterbox.power.phone.app.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.OrderEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-06.
 * Description
 */
@Route(path = ARouteHelper.ORDER_DETAIL)
public class OrderDetailActivity extends NavigationActivity {
    @Autowired
    public OrderEntity orderEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_order_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.order_14);
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (orderEntity != null) {
            ((TextView) findViewById(R.id.act_order_detail_no_tv)).setText(getString(R.string.order_5, StringUtil.fixNullStr(orderEntity.code)));
            ((TextView) findViewById(R.id.act_order_detail_state_tv)).setText(orderEntity.getState(this));
            if (orderEntity.state == 2) {
                ((TextView) findViewById(R.id.act_order_detail_state_tv)).setTextColor(Color.parseColor("#37b737"));
            } else {
                ((TextView) findViewById(R.id.act_order_detail_state_tv)).setTextColor(Color.parseColor("#EE8822"));
            }
            ((TextView) findViewById(R.id.act_order_detail_rent_time_tv)).setText(StringUtil.fixNullStr(orderEntity.rentTime));
            ((TextView) findViewById(R.id.act_order_detail_rent_address_tv)).setText(StringUtil.fixNullStr(orderEntity.rentShopName));
            ((TextView) findViewById(R.id.act_order_detail_return_time_tv)).setText(StringUtil.fixNullStr(orderEntity.returnTime));
            ((TextView) findViewById(R.id.act_order_detail_return_address_tv)).setText(StringUtil.fixNullStr(orderEntity.returnShopName));
            ((TextView) findViewById(R.id.act_order_detail_need_cost_price_tv)).setText(getString(R.string.order_33, CnyUtil.getPriceByUnit(this, orderEntity.price)));
            if (orderEntity.state == 1 && orderEntity.sysTime > 0) {
                //租借中
                ((TextView) findViewById(R.id.act_order_detail_use_time_tv)).setText((long) orderEntity.sysTime / 60 + getString(R.string.order_42));
            } else {
                ((TextView) findViewById(R.id.act_order_detail_use_time_tv)).setText(orderEntity.useTime + getString(R.string.order_42));
            }

            ((TextView) findViewById(R.id.act_order_detail_fee_time_tv)).setText(orderEntity.freeTime + getString(R.string.order_42));
            ((TextView) findViewById(R.id.act_order_detail_deduction_time_tv)).setText(StringUtil.fixNullStr(orderEntity.deductionTime, "0") + getString(R.string.order_42));
            ((TextView) findViewById(R.id.act_order_detail_max_price_day_tv)).setText(CnyUtil.getPriceByUnit(this, orderEntity.highCost));
            ((TextView) findViewById(R.id.act_order_detail_pay_price_tv)).setText(CnyUtil.getPriceByUnit(this, orderEntity.cost));
            ((TextView) findViewById(R.id.act_order_detail_pay_state_tv)).setText(orderEntity.getPayState(this));
            ((TextView) findViewById(R.id.act_order_detail_pay_type_tv)).setText(orderEntity.getPayType(this));
//            ((TextView) findViewById(R.id.act_order_detail_depoit_cost_tv)).setText(CnyUtil.getPriceByUnit(this, orderEntity.depoitCost));
//            ((TextView) findViewById(R.id.act_order_detail_depoit_state_tv)).setText(orderEntity.getDepoitState(this));
//            ((TextView) findViewById(R.id.act_order_detail_depoit_type_tv)).setText(orderEntity.getDepoitType(this));
            ((TextView) findViewById(R.id.act_order_detail_rule_tv)).setText(getString(R.string.order_43, String.valueOf(orderEntity.freeTime), CnyUtil.getPrice(orderEntity.price), CnyUtil.getPrice(orderEntity.highCost)));
            if (orderEntity.state == 2 && orderEntity.payState == 2) {
                findViewById(R.id.act_order_detail_ex_tv).setVisibility(View.GONE);
            } else {
                findViewById(R.id.act_order_detail_ex_tv).setVisibility(View.VISIBLE);
                findViewById(R.id.act_order_detail_ex_tv).setOnClickListener(v -> {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(getString(R.string.order_29));
                    list.add(getString(R.string.order_4));
                    DialogUtils.showListDialog(OrderDetailActivity.this, null, list, position -> {
                        if (position == 0) {
                            DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.order_30), getString(R.string.app_16), v12 -> {

                            }, getString(R.string.order_37), v1 -> buy(orderEntity.orderId));
                        } else {
                            ARouteHelper.helper_detail("0", "0").navigation();
                        }
                    });
                });
            }
        }
    }

    private void buy(long orderId) {
        HttpClient.getInstance().order_purchase(String.valueOf(orderId), new NormalHttpCallBack<ResponseEntity>(OrderDetailActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                DialogUtils.showDialog(getSupportFragmentManager(), null, StringUtil.fixNullStr(responseEntity.getMsg()), getString(R.string.app_11), v -> finish()).setCancelable(false);
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(OrderDetailActivity.this, msg);
            }
        });
    }
}
