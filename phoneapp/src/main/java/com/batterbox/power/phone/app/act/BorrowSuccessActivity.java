package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.OrderStateEntity;
import com.batterbox.power.phone.app.http.DomainHelper;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;

/**
 * Created by ass on 2019-07-31.
 * Description
 */
@Route(path = ARouteHelper.BORROW_SUCCESS)
public class BorrowSuccessActivity extends NavigationActivity {
    @Autowired
    public OrderStateEntity orderStateEntity;
    @Autowired
    public DeviceEntity deviceEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_borrow_success;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (orderStateEntity != null) {
            ((TextView) findViewById(R.id.act_borrow_success_hour_cost_tv)).setText(getString(R.string.borrow_24, CnyUtil.getPriceByUnit(this, orderStateEntity.price)));
            if (StringUtil.isEquals(LanguageUtil.ES,LanguageUtil.getLanguage())){
                ((TextView) findViewById(R.id.act_borrow_success_des_tv1)).setText(getString(R.string.borrow_22_1, StringUtil.fixNullStr(orderStateEntity.freeMinute, "0"), CnyUtil.getPrice(orderStateEntity.price), CnyUtil.getPrice(orderStateEntity.highCost),CnyUtil.getPrice(orderStateEntity.deposit)));
            }else{
                ((TextView) findViewById(R.id.act_borrow_success_des_tv1)).setText(getString(R.string.borrow_22, StringUtil.fixNullStr(orderStateEntity.freeMinute, "0"), CnyUtil.getPrice(orderStateEntity.price), CnyUtil.getPrice(orderStateEntity.highCost)));
            }

//            ((TextView) findViewById(R.id.act_borrow_success_des_tv2)).setText(getString(R.string.borrow_23, CnyUtil.getPrice(orderStateEntity.deposit), CnyUtil.getPrice(orderStateEntity.deposit)));
            findViewById(R.id.act_borrow_result_help_tv).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getUserAgreementH5()).navigation());
        }
        if (deviceEntity != null) {

            ((TextView) findViewById(R.id.act_device_detail_fee_tv)).setText(getString(R.string.borrow_3, StringUtil.fixNullStr(deviceEntity.giveTime)));
            ((TextView) findViewById(R.id.act_device_detail_rentCost_tv)).setText(getString(R.string.borrow_5, CnyUtil.getPriceByUnit(this, deviceEntity.rentCost)));
            ((TextView) findViewById(R.id.act_device_detail_highCost_tv)).setText(getString(R.string.borrow_10, CnyUtil.getPriceByUnit(this, deviceEntity.highCost)));
            ((TextView) findViewById(R.id.act_device_detail_highCost_day_tv)).setText(getString(R.string.borrow_6, CnyUtil.getPriceByUnit(this, deviceEntity.highCost)));
            ((TextView) findViewById(R.id.act_device_detail_more_than_tv)).setText(getString(R.string.borrow_7, CnyUtil.getPriceByUnit(this, deviceEntity.depoitCost)));
            ((TextView) findViewById(R.id.act_device_detail_more_than_des_tv)).setText(getString(R.string.borrow_8, CnyUtil.getPriceByUnit(this, deviceEntity.depoitCost)));

        }
    }
}
