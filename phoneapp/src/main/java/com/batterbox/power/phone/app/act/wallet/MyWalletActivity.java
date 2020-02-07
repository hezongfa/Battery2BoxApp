package com.batterbox.power.phone.app.act.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CardEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-08-03.
 * Description
 */
@Route(path = ARouteHelper.WALLET_MY)
public class MyWalletActivity extends NavigationActivity {
    CardEntity cardEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_wallet;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        setNavigationVisible(false);
//        initNavigationRight(getString(R.string.wallet_1), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouteHelper.user_bill().navigation();
//            }
//        });
        findViewById(R.id.act_my_wallet_back_btn).setOnClickListener(v -> finish());
        findViewById(R.id.act_my_wallet_cart_submit_tv).setOnClickListener(v -> ARouteHelper.mycard().navigation());
        findViewById(R.id.act_my_wallet_balance_submit_tv).setOnClickListener(v -> recharge_getRechargeTable());
        findViewById(R.id.act_my_wallet_time_submit_tv).setOnClickListener(v -> ARouteHelper.time_record().navigation());
        findViewById(R.id.act_my_wallet_bill_submit_tv).setOnClickListener(v -> ARouteHelper.user_bill().navigation());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardEntity = null;
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ((TextView) findViewById(R.id.act_my_wallet_balance_tv)).setText(CnyUtil.getPriceByUnit(this, userEntity.balance));
            ((TextView) findViewById(R.id.act_my_wallet_time_tv1)).setText(userEntity.remainingTime + getString(R.string.order_42));
        }
        HttpClient.getInstance().us_queryCard(new NormalHttpCallBack<ResponseEntity<CardEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<CardEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null && !StringUtil.isEmpty(responseEntity.getData().cardNum)) {
                    cardEntity = responseEntity.getData();
                    ((TextView) findViewById(R.id.act_my_wallet_card_tv1)).setText(StringUtil.fixNullStr(cardEntity.cardNum));
                    ((TextView) findViewById(R.id.act_my_wallet_card_tv2)).setText(StringUtil.fixNullStr(cardEntity.cardType));
                }
            }

            @Override
            public void onFail(ResponseEntity<CardEntity> responseEntity, String msg) {

            }
        });
    }

    private void recharge_getRechargeTable() {
        ARouteHelper.recharge_list().navigation();
//        if (cardEntity != null) {
//            HttpClient.getInstance().recharge_getRechargeTable(cardEntity.mcId, new NormalHttpCallBack<ResponseEntity<String>>(this) {
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onSuccess(ResponseEntity<String> responseEntity) {
//                    if (responseEntity != null && !StringUtil.isEmpty(responseEntity.getData())) {
//                        ARouteHelper.hybrid_nav(responseEntity.getData()).navigation();
//                    }
//                }
//
//                @Override
//                public void onFail(ResponseEntity<String> responseEntity, String msg) {
//                    FQT.showShort(MyWalletActivity.this, msg);
//                }
//            });
//        }
    }
}
