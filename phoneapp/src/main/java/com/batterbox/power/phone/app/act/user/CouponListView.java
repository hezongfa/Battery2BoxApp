package com.batterbox.power.phone.app.act.user;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.entity.CouponEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BasePagerListView;
import com.chenyi.baselib.ui.NavTabPagerViewI;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2020-02-07.
 * Description
 */
public class CouponListView extends BasePagerListView<CouponEntity> implements NavTabPagerViewI {
    //1(充电优惠卷)，0(商家优惠卷)
    int type = 1;

    public CouponListView(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected QuickDelegateAdapter<CouponEntity> getAdapter() {
        return new QuickDelegateAdapter<CouponEntity>(getContext(), R.layout.item_dialog_shop_coupon) {
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
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().cp_myCoupons(type, page, pageSize, new NormalHttpCallBack<ResponseEntity<ArrayList<CouponEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<CouponEntity>> responseEntity) {
                if (responseEntity != null) {
                    handleListData(responseEntity.getData());
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<CouponEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }

    @Override
    public View getContentView() {
        return this;
    }

    @Override
    public void onCreateView(Context context) {

    }
}
