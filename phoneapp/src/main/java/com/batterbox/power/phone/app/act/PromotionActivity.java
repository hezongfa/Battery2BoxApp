package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.dialog.ShopCouponDialog;
import com.batterbox.power.phone.app.entity.LbsShopCouponEntity;
import com.batterbox.power.phone.app.entity.LocMap;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.LocMapUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.MathsUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
@Route(path = ARouteHelper.PROMOTION)
public class PromotionActivity extends NavListActivity<LbsShopCouponEntity> {
    String key = "";

    @Override
    protected int getTopLayId() {
        return R.layout.lay_promotion_bar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        setNavigationVisible(false);
        findViewById(R.id.lay_promotion_barback_btn).setOnClickListener(v -> finish());
        EditText searchEt = findViewById(R.id.lay_promotion_baret);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                key = searchEt.getText().toString();
                refresh();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected QuickDelegateAdapter<LbsShopCouponEntity> getAdapter() {
        return new QuickDelegateAdapter<LbsShopCouponEntity>(this, R.layout.item_promotion) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, LbsShopCouponEntity item, int viewType) {
                ImageLoaderUtil.load_round(context, StringUtil.fixNullStr(item.shopIco), holder.getView(R.id.item_promotion_shop_iv), 5);
                holder.setText(R.id.item_promotion_shop_name_tv, StringUtil.fixNullStr(item.shopName));
                holder.setText(R.id.item_promotion_time_tv, StringUtil.fixNullStr(item.businessTime));
                holder.setText(R.id.item_promotion_location_tv, StringUtil.fixNullStr(item.shopAdress));
                holder.setText(R.id.item_promotion_coupon_tv, StringUtil.fixNullStr(item.couponRemarke));
                holder.setText(R.id.item_promotion_dis_tv, StringUtil.fixNullStr(item.distance));
                holder.setOnClickListener(R.id.item_promotion_dis_tv, v -> {
                    List<LocMap> locMaps = LocMapUtil.getLocMapList(PromotionActivity.this);
                    if (locMaps.size() > 0) {
                        ArrayList<String> list = new ArrayList<>();
                        for (LocMap locMap : locMaps) {
                            list.add(locMap.appName);
                        }
                        DialogUtils.showListDialog(PromotionActivity.this, null, list, position -> {
                            LocMap locMap = locMaps.get(position);
                            LocMapUtil.navigationByLocMap(PromotionActivity.this, MathsUtil.round(item.la, 6), MathsUtil.round(item.lo, 6), StringUtil.fixNullStr(item.shopName, "location"), StringUtil.fixNullStr(item.shopAdress), locMap.packageName);
                        });
                    } else {
                        FQT.showShort(PromotionActivity.this, "no map app client");
                    }
                });
                holder.itemView.setOnClickListener(v -> {
                    ShopCouponDialog shopCouponDialog = new ShopCouponDialog();
                    shopCouponDialog.setShopEntity(item);
                    shopCouponDialog.show(getSupportFragmentManager());
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
        HttpClient.getInstance().bs_findShopCoupons(key, BatterBoxApp.lat, BatterBoxApp.lng, new NormalHttpCallBack<ResponseEntity<ArrayList<LbsShopCouponEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<LbsShopCouponEntity>> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    handleListData(responseEntity.getData());
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<LbsShopCouponEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }
}
