package com.batterbox.power.phone.app.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CouponEntity;
import com.batterbox.power.phone.app.entity.LbsShopCouponEntity;
import com.batterbox.power.phone.app.entity.ShopDetailEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.dialog.BaseDialogFragment;
import com.chenyi.baselib.widget.recycleviewadapter.BaseViewHolder;
import com.chenyi.baselib.widget.recycleviewadapter.QuickRecycleAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-04.
 * Description
 */
public class ShopCouponDialog extends BaseDialogFragment {
    ShopDetailEntity shopEntity;
    LbsShopCouponEntity lbsShopCouponEntity;
    QuickRecycleAdapter<CouponEntity> adapter;
    ImageView iv;
    TextView titleTv;

    public void setLbsShopCouponEntity(LbsShopCouponEntity lbsShopCouponEntity) {
        this.lbsShopCouponEntity = lbsShopCouponEntity;
    }

    public void setShopEntity(ShopDetailEntity shopEntity) {
        this.shopEntity = shopEntity;
    }

    @Override
    protected Dialog dialog() {
        Dialog dialog = getBaseDialog();
        dialog.setContentView(R.layout.dialog_shop_coupon);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setWindowAnimations(R.style.BottomDialog);
        dialog.findViewById(R.id.dialog_shop_coupon_close_iv).setOnClickListener(v -> dismissAllowingStateLoss());
        iv = dialog.findViewById(R.id.dialog_shop_coupon_iv);
        titleTv = dialog.findViewById(R.id.dialog_shop_coupon_title_tv);
        RecyclerView rv = dialog.findViewById(R.id.dialog_shop_coupon_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new QuickRecycleAdapter<CouponEntity>(getContext(), R.layout.item_dialog_shop_coupon) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, CouponEntity item, int viewType) {
                ImageLoaderUtil.load(context, StringUtil.fixNullStr(item.img), holder.getView(R.id.item_dialog_shop_coupon_iv));
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_iv, v -> {
                    if (!StringUtil.isEmpty(item.img)) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add(item.img);
                        ARouteHelper.show_big_imgs(list, item.img).navigation();
                    }
                });
                holder.setText(R.id.item_dialog_shop_coupon_price_tv, StringUtil.fixNullStr(item.typeValue));
                holder.setText(R.id.item_dialog_shop_coupon_type_center_tv, StringUtil.fixNullStr(item.typeCenterName));
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
        });
        if (shopEntity != null) {
            titleTv.setText(StringUtil.fixNullStr(shopEntity.shopName));
            ImageLoaderUtil.load_round(getContext(), shopEntity.shopIco, iv, 8);
            HttpClient.getInstance().bs_findCouponsByShop(shopEntity.shopId, new NormalHttpCallBack<ResponseEntity<ArrayList<CouponEntity>>>(getContext()) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<ArrayList<CouponEntity>> responseEntity) {
                    adapter.replaceAll(responseEntity.getData());
                }

                @Override
                public void onFail(ResponseEntity<ArrayList<CouponEntity>> responseEntity, String msg) {

                }
            });
        } else if (lbsShopCouponEntity != null) {
            titleTv.setText(StringUtil.fixNullStr(lbsShopCouponEntity.shopName));
            ImageLoaderUtil.load_round(getContext(), lbsShopCouponEntity.shopIco, iv, 20);
            adapter.replaceAll(lbsShopCouponEntity.shopCoupons);
        }
        return dialog;
    }

    public void show(FragmentManager manager) {
        super.show(manager, getClass().getName());
    }
}
