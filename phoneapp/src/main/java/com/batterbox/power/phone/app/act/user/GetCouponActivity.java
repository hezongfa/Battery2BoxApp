package com.batterbox.power.phone.app.act.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CouponEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.ScanBarCodeHelper;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2020-02-07.
 * Description
 */
@Route(path = ARouteHelper.COUPON_GET)
public class GetCouponActivity extends NavListActivity<CouponEntity> {
    EditText searchEt;

    @Override
    protected int getTopLayId() {
        return R.layout.lay_coupon_get_top;
    }

    @Override
    protected int getBottomLayId() {
        return R.layout.lay_coupon_get_bottom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationVisible(false);
        StatusBarCompat.translucentStatusBar(this, true);
        ((TextView) findViewById(R.id.lay_sw_nav_bar_tv)).setText(R.string.coupon_6);
        ((TextView) findViewById(R.id.lay_sw_nav_bar_sub_tv)).setText(R.string.coupon_2);
        findViewById(R.id.lay_sw_nav_bar_back_btn).setOnClickListener(v -> finish());
        setRefreshEnable(false);
        setLoadMoreEnable(false);
        searchEt = findViewById(R.id.lay_coupon_get_top_et);
//        searchEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                searchCode();
//            }
//        });
        findViewById(R.id.lay_coupon_get_top_scan_iv).setOnClickListener(v -> ScanBarCodeHelper.scan(GetCouponActivity.this, result -> {
            if (!StringUtil.isEmpty(result.getContent())) {
                searchCode(result.getContent());
            }
        }));
        findViewById(R.id.lay_coupon_get_top_btn).setOnClickListener(v -> searchCode(searchEt.getText().toString()));
        bottomLay.setVisibility(View.GONE);
        findViewById(R.id.lay_coupon_get_bottom_clear_btn).setOnClickListener(v -> {
            searchEt.setText("");
            adapter.clear();
            bottomLay.setVisibility(View.GONE);
        });
        findViewById(R.id.lay_coupon_get_bottom_use_btn).setOnClickListener(v -> {
            if (adapter.getItemCount() > 0) {
                CouponEntity couponEntity = adapter.getItem(0);
                if (couponEntity != null) {
                    HttpClient.getInstance().cp_useMyCoupon(couponEntity.id, new NormalHttpCallBack<ResponseEntity>(GetCouponActivity.this) {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(ResponseEntity responseEntity) {
                            FQT.showShort(GetCouponActivity.this, getString(R.string.app_20));

                        }

                        @Override
                        public void onFail(ResponseEntity responseEntity, String msg) {
                            FQT.showShort(GetCouponActivity.this, msg);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected QuickDelegateAdapter getAdapter() {
        return new QuickDelegateAdapter<CouponEntity>(this, R.layout.item_dialog_shop_coupon) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, CouponEntity item, int viewType) {
                ImageLoaderUtil.load(context, StringUtil.fixNullStr(item.img), holder.getView(R.id.item_dialog_shop_coupon_iv));
                holder.setText(R.id.item_dialog_shop_coupon_price_tv, StringUtil.fixNullStr(item.typeValue) + "" + context.getString(R.string.m_1));
                holder.setText(R.id.item_dialog_shop_coupon_type_tv, StringUtil.fixNullStr(item.typeName));
                holder.setText(R.id.item_dialog_shop_coupon_name_tv, StringUtil.fixNullStr(item.name));
                holder.setText(R.id.item_dialog_shop_coupon_time_tv, StringUtil.fixNullStr(item.registtime) + " - " + StringUtil.fixNullStr(item.effectiveTime));
                holder.setText(R.id.item_dialog_shop_coupon_des_tv, StringUtil.fixNullStr(item.remarke));
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_rl, v -> {
                    item.isExpain = !item.isExpain;
                    if (item.isExpain) {
                        ((TextView) holder.getView(R.id.item_dialog_shop_coupon_rule_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pick, 0);
                        holder.setVisible(R.id.item_dialog_shop_coupon_des_tv, true);
                    } else {
                        holder.setVisible(R.id.item_dialog_shop_coupon_des_tv, false);
                        ((TextView) holder.getView(R.id.item_dialog_shop_coupon_rule_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_expain, 0);
                    }
                });
                if (item.isExpain) {
                    ((TextView) holder.getView(R.id.item_dialog_shop_coupon_rule_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pick, 0);
                    holder.setVisible(R.id.item_dialog_shop_coupon_des_tv, true);

                } else {
                    holder.setVisible(R.id.item_dialog_shop_coupon_des_tv, false);
                    ((TextView) holder.getView(R.id.item_dialog_shop_coupon_rule_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_expain, 0);
                }
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }

        };
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);

    }

    private void searchCode(String code) {
        HttpClient.getInstance().cp_findCouponByCode(code, new NormalHttpCallBack<ResponseEntity<CouponEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<CouponEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    adapter.clear();
                    adapter.add(responseEntity.getData());
                    bottomLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(ResponseEntity<CouponEntity> responseEntity, String msg) {
                bottomLay.setVisibility(View.GONE);
                FQT.showShort(GetCouponActivity.this, msg);
            }
        });
    }

    @Override
    protected void getData(int page, int pageSize) {

    }
}
