package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.dialog.ShopCouponDialog;
import com.batterbox.power.phone.app.entity.LbsShopCouponEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
@Route(path = ARouteHelper.PROMOTION)
public class PromotionActivity extends NavListActivity<LbsShopCouponEntity> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_13);
    }

    @Override
    protected QuickDelegateAdapter<LbsShopCouponEntity> getAdapter() {
        return new QuickDelegateAdapter<LbsShopCouponEntity>(this, R.layout.item_activity_shop) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, LbsShopCouponEntity item, int viewType) {
                ImageLoaderUtil.load_round(context, item.shopIco, holder.getView(R.id.item_activity_shop_iv), 8);
                holder.setText(R.id.item_activity_shop_name_tv, StringUtil.fixNullStr(item.shopName));
                holder.itemView.setOnClickListener(v -> {
                    ShopCouponDialog shopCouponDialog = new ShopCouponDialog();
                    shopCouponDialog.setLbsShopCouponEntity(item);
                    shopCouponDialog.show(getSupportFragmentManager());
                });
                holder.setVisible(R.id.item_activity_shop_tv1, false);
                holder.setVisible(R.id.item_activity_shop_tv2, false);
                holder.setVisible(R.id.item_activity_shop_tv3, false);
                if (!StringUtil.isEmpty(item.shopCoupons)) {
                    if (item.shopCoupons.size() > 0 && item.shopCoupons.get(0) != null) {
                        holder.setVisible(R.id.item_activity_shop_tv1, true);
                        holder.setText(R.id.item_activity_shop_tv1, StringUtil.fixNullStr(item.shopCoupons.get(0).name));
                    }
                    if (item.shopCoupons.size() > 1 && item.shopCoupons.get(1) != null) {
                        holder.setVisible(R.id.item_activity_shop_tv2, true);
                        holder.setText(R.id.item_activity_shop_tv2, StringUtil.fixNullStr(item.shopCoupons.get(1).name));
                    }
                    if (item.shopCoupons.size() > 2 && item.shopCoupons.get(2) != null) {
                        holder.setVisible(R.id.item_activity_shop_tv3, true);
                        holder.setText(R.id.item_activity_shop_tv3, StringUtil.fixNullStr(item.shopCoupons.get(2).name));
                    }
                }
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().bs_findShopCoupons(BatterBoxApp.lat, BatterBoxApp.lng, new NormalHttpCallBack<ResponseEntity<ArrayList<LbsShopCouponEntity>>>(this) {
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
