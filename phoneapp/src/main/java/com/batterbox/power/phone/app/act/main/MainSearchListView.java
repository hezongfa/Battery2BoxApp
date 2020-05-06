package com.batterbox.power.phone.app.act.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BasePagerListView;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ass on 2020-05-06.
 * Description
 */
public class MainSearchListView extends BasePagerListView<LBShopEntity> {

    ArrayList<LBShopEntity> lbShopEntityArrayList = new ArrayList<>();

    public MainSearchListView(Context context) {
        super(context);
        setLoadMoreEnable(false);
    }

    public MainSearchListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLoadMoreEnable(false);
    }

    @Override
    protected QuickDelegateAdapter<LBShopEntity> getAdapter() {
        return new QuickDelegateAdapter<LBShopEntity>(getContext(), R.layout.item_shop) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, LBShopEntity lbShopEntity, int viewType) {
                ImageLoaderUtil.load_round(context, lbShopEntity.shopIco, holder.getView(R.id.item_shop_iv), ViewUtil.getDimen(context, R.dimen.x16));
                holder.setText(R.id.item_shop_name_tv, StringUtil.fixNullStr(lbShopEntity.shopName));
                holder.setText(R.id.item_shop_time_tv, StringUtil.fixNullStr(lbShopEntity.businessTime));
                holder.setText(R.id.item_shop_address_tv, StringUtil.fixNullStr(lbShopEntity.shopAdress));
                holder.setText(R.id.item_shop_borrow_count_tv, context.getString(R.string.shop_1, lbShopEntity.rentCount));
                holder.setText(R.id.item_shop_return_count_tv, context.getString(R.string.shop_2, lbShopEntity.returnCount));
                holder.setText(R.id.item_shop_dis_tv, lbShopEntity.dis + "km");
                holder.itemView.setOnClickListener(v -> ARouteHelper.shop_detail(lbShopEntity).navigation());
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }


    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().bs(BatterBoxApp.lat, BatterBoxApp.lng, new NormalHttpCallBack<ResponseEntity<ArrayList<LBShopEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<LBShopEntity>> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
//                    handleListData(responseEntity.getData());
                    fillterText(key, responseEntity.getData());
                }
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<LBShopEntity>> responseEntity, String msg) {

            }
        });
    }

    String key;

    public void refreshData(String key, List<LBShopEntity> lbShopEntities) {
        this.key = key;
        if (StringUtil.isEmpty(lbShopEntities)) {
            page = 1;
            refresh();
        } else {
            fillterText(key, lbShopEntities);
        }
    }

    private void fillterText(String text, List<LBShopEntity> lbShopEntities) {
        this.lbShopEntityArrayList.clear();
        if (lbShopEntities != null) {
            lbShopEntityArrayList.addAll(lbShopEntities);
        }
        if (StringUtil.isEmpty(text)) {
            adapter.replaceAll(lbShopEntityArrayList);
            return;
        }
        ArrayList<LBShopEntity> list = new ArrayList<>();
        for (LBShopEntity lbShopEntity : lbShopEntityArrayList) {
            if ((!StringUtil.isEmpty(lbShopEntity.shopName)) && lbShopEntity.shopName.toLowerCase().contains(text.toLowerCase())) {
                list.add(lbShopEntity);
            }
//            else if ((!StringUtil.isEmpty(lbShopEntity.shopAdress)) && lbShopEntity.shopAdress.contains(text)) {
//                list.add(lbShopEntity);
//            }
        }
        adapter.replaceAll(list);
    }

    public List<LBShopEntity> getListData() {
        return lbShopEntityArrayList;
    }
}
