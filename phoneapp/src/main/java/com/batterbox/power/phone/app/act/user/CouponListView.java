package com.batterbox.power.phone.app.act.user;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CouponEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.BasePagerListView;
import com.chenyi.baselib.ui.NavTabPagerViewI;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2020-02-07.
 * Description
 */
public class CouponListView extends BasePagerListView<CouponEntity> implements NavTabPagerViewI {
    //1(充电优惠卷)，0(商家优惠卷)
    int type;

    public CouponListView(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected QuickDelegateAdapter<CouponEntity> getAdapter() {
        return new QuickDelegateAdapter<CouponEntity>(getContext(), R.layout.item_dialog_shop_coupon) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, CouponEntity item, int viewType) {
                holder.setVisible(R.id.item_dialog_shop_coupon_delete_iv, true);
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_delete_iv, v -> delete(item.id));
                if (item.isUse != 1) {
                    holder.setVisible(R.id.item_dialog_shop_coupon_unable_iv, true);
                } else {
                    holder.setVisible(R.id.item_dialog_shop_coupon_unable_iv, false);
                }
                ImageLoaderUtil.load(context, StringUtil.fixNullStr(item.img), holder.getView(R.id.item_dialog_shop_coupon_iv));
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_iv, v -> {
                    if (item.isUse != 1) {
                        return;
                    }
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
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_rule_tv, v -> {
                    if (item.isUse != 1) {
                        return;
                    }
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
                holder.setOnClickListener(R.id.item_dialog_shop_coupon_rl, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isUse != 1) {
                            return;
                        }
                        use(item.id);
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

    private void delete(long id) {
        DialogUtils.showDialog(((BaseActivity) getContext()).getSupportFragmentManager(), null, getContext().getString(R.string.app_22), getContext().getString(R.string.app_16), v -> {

        }, getContext().getString(R.string.coupon_14), new OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient.getInstance().cp_deleMyCoupon(id, new NormalHttpCallBack<ResponseEntity>(getContext()) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        refresh();
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(getContext(), msg);
                    }
                });
            }
        });
    }

    private void use(long id) {
        DialogUtils.showDialog(((BaseActivity) getContext()).getSupportFragmentManager(), null, getContext().getString(R.string.coupon_13), getContext().getString(R.string.app_16), v -> {

        }, getContext().getString(R.string.coupon_10), new OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient.getInstance().cp_useMyCoupon(id, new NormalHttpCallBack<ResponseEntity>(getContext()) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        refresh();
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(getContext(), msg);
                    }
                });
            }
        });
    }

}
