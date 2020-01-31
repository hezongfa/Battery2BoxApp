package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.chenyi.baselib.ui.SearchListActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-07.
 * Description
 */
@Route(path = ARouteHelper.SHOP_LIST)
public class ShopListActivity extends SearchListActivity<LBShopEntity> {

    @Autowired
    public ArrayList<LBShopEntity> lbShopEntities;

    @Override
    protected QuickDelegateAdapter<LBShopEntity> getAdapter() {
        return new QuickDelegateAdapter<LBShopEntity>(this, R.layout.item_shop) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, LBShopEntity lbShopEntity, int viewType) {
                ImageLoaderUtil.load_round(context, lbShopEntity.shopIco, holder.getView(R.id.item_shop_iv), ViewUtil.getDimen(context, R.dimen.x16));
                holder.setText(R.id.item_shop_name_tv, StringUtil.fixNullStr(lbShopEntity.shopName));
                holder.setText(R.id.item_shop_time_tv, StringUtil.fixNullStr(lbShopEntity.businessTime));
                holder.setText(R.id.item_shop_address_tv, StringUtil.fixNullStr(lbShopEntity.shopAdress));
                holder.setText(R.id.item_shop_borrow_count_tv, getString(R.string.shop_1, lbShopEntity.rentCount));
                holder.setText(R.id.item_shop_return_count_tv, getString(R.string.shop_2, lbShopEntity.returnCount));
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchEt.setHint(R.string.main_1);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fillterText(searchEt.getText().toString());
            }
        });
        setRefreshEnable(false);
        setLoadMoreEnable(false);

        if (lbShopEntities != null) {
            adapter.replaceAll(lbShopEntities);
        }
    }

    @Override
    protected void getData(int page, int pageSize) {
        fillterText(searchEt.getText().toString());
    }

    private void fillterText(String text) {
        if (StringUtil.isEmpty(text)) {
            adapter.replaceAll(lbShopEntities);
            return;
        }
        ArrayList<LBShopEntity> list = new ArrayList<>();
        for (LBShopEntity lbShopEntity : lbShopEntities) {
            if ((!StringUtil.isEmpty(lbShopEntity.shopName)) && lbShopEntity.shopName.toLowerCase().contains(text.toLowerCase())) {
                list.add(lbShopEntity);
            }
//            else if ((!StringUtil.isEmpty(lbShopEntity.shopAdress)) && lbShopEntity.shopAdress.contains(text)) {
//                list.add(lbShopEntity);
//            }
        }
        adapter.replaceAll(list);
    }
}
