package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.TimeRecordEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
@Route(path = ARouteHelper.TIME_RECORD)
public class TimeRecordListActivity extends NavListActivity<TimeRecordEntity> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.wallet_21);
    }

    @Override
    protected QuickDelegateAdapter<TimeRecordEntity> getAdapter() {
        return new QuickDelegateAdapter<TimeRecordEntity>(this, R.layout.item_use_time) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, TimeRecordEntity item, int viewType) {
                holder.setText(R.id.item_use_time_order_tv, getString(R.string.wallet_22, StringUtil.fixNullStr(item.mfdCode)));
                holder.setText(R.id.item_use_time_d_time_tv, (item.mfdType == 1 ? "+" : "-") + StringUtil.fixNullStr(item.mfdChange) + getString(R.string.order_42));
                holder.setText(R.id.item_use_time_des_tv, StringUtil.fixNullStr(item.transactionType));
                holder.setText(R.id.item_use_time_c_tv, StringUtil.fixNullStr(item.bshopName));
                holder.setText(R.id.item_use_time_time_tv, StringUtil.fixNullStr(item.transactionTime));
                holder.setText(R.id.item_use_time_l_time_tv, getString(R.string.wallet_23, StringUtil.fixNullStr(item.mfdNow)));
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().us_freetimeDetail(page, pageSize, new NormalHttpCallBack<ResponseEntity<ArrayList<TimeRecordEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<TimeRecordEntity>> responseEntity) {
                if (responseEntity != null) {
                    handleListData(responseEntity.getData());
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<TimeRecordEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }
}
