package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.BorrowResultEntity;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-07-30.
 * Description
 */
@Route(path = ARouteHelper.DEVICE_DETAIL)
public class DeviceDetailActivity extends NavigationActivity {
    @Autowired
    public DeviceEntity deviceEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_device_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.borrow_1);
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (deviceEntity != null) {

            ((TextView) findViewById(R.id.act_device_detail_fee_tv)).setText(getString(R.string.borrow_3, StringUtil.fixNullStr(deviceEntity.giveTime)));
            ((TextView) findViewById(R.id.act_device_detail_rentCost_tv)).setText(getString(R.string.borrow_5, CnyUtil.getPriceByUnit(this, deviceEntity.rentCost)));
            ((TextView) findViewById(R.id.act_device_detail_highCost_tv)).setText(getString(R.string.borrow_10, CnyUtil.getPriceByUnit(this, deviceEntity.highCost)));
            ((TextView) findViewById(R.id.act_device_detail_highCost_day_tv)).setText(getString(R.string.borrow_6, CnyUtil.getPriceByUnit(this, deviceEntity.highCost)));
            ((TextView) findViewById(R.id.act_device_detail_more_than_tv)).setText(getString(R.string.borrow_7, CnyUtil.getPriceByUnit(this, deviceEntity.depoitCost)));
            ((TextView) findViewById(R.id.act_device_detail_more_than_des_tv)).setText(getString(R.string.borrow_8, CnyUtil.getPriceByUnit(this, deviceEntity.depoitCost)));

            findViewById(R.id.act_device_detail_btn).setOnClickListener(v -> HttpClient.getInstance().order_borrow(deviceEntity.boxCode, new NormalHttpCallBack<ResponseEntity<BorrowResultEntity>>(DeviceDetailActivity.this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<BorrowResultEntity> responseEntity) {
                    if (responseEntity != null && responseEntity.getData() != null) {
                        ARouteHelper.borrow_result(responseEntity.getData(),deviceEntity).navigation();
                        finish();
                    }
                }

                @Override
                public void onFail(ResponseEntity<BorrowResultEntity> responseEntity, String msg) {
                    FQT.showShort(DeviceDetailActivity.this, msg);
                    if (responseEntity.getCode() == 10108) {
                        ARouteHelper.wallet_card_edit().navigation();
                    }
                }
            }));
        }
    }
}
