package com.batterbox.power.phone.app.act.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CardEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;

/**
 * Created by ass on 2019-10-09.
 * Description
 */
@Route(path = ARouteHelper.MYCARD)
public class MyCardActivity extends NavigationActivity {
    CardEntity cardEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_card;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.wallet_24);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cardEntity = null;
        HttpClient.getInstance().us_queryCard(new NormalHttpCallBack<ResponseEntity<CardEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<CardEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null && !StringUtil.isEmpty(responseEntity.getData().cardNum)) {
                    findViewById(R.id.act_my_card_rl).setVisibility(View.VISIBLE);
                    ((Button) findViewById(R.id.act_my_card_btn)).setText(R.string.wallet_17);
                    cardEntity = responseEntity.getData();
                    ((TextView) findViewById(R.id.act_my_card_tv1)).setText(StringUtil.fixNullStr(cardEntity.cardType));
                    ((TextView) findViewById(R.id.act_my_card_tv2)).setText(StringUtil.fixNullStr(cardEntity.cardNum));
                    if (StringUtil.isEquals("VISA", cardEntity.cardType)) {
                        ((ImageView) findViewById(R.id.act_my_card_iv)).setImageResource(R.mipmap.pic_card_visa);
                    } else if (StringUtil.isEquals("MC", cardEntity.cardType)) {
                        ((ImageView) findViewById(R.id.act_my_card_iv)).setImageResource(R.mipmap.pic_card_zhifupingtaimaster);
                    } else {
                        ((ImageView) findViewById(R.id.act_my_card_iv)).setImageResource(R.mipmap.pic_card_xinyongqiazhifu);
                    }
                    findViewById(R.id.act_my_card_btn).setOnClickListener(v -> {
                        DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.wallet_18), getString(R.string.app_16), v1 -> {

                        }, getString(R.string.app_32), v12 -> {
                            del(cardEntity.mcId);
                        });
                    });
                } else {
                    findViewById(R.id.act_my_card_rl).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.act_my_card_btn)).setText(R.string.wallet_25);
                    findViewById(R.id.act_my_card_btn).setOnClickListener(v -> ARouteHelper.wallet_card_edit().navigation());
                }

            }

            @Override
            public void onFail(ResponseEntity<CardEntity> responseEntity, String msg) {
                FQT.showShort(MyCardActivity.this, msg);
            }
        });
    }

    private void del(String mcId) {
        HttpClient.getInstance().us_deleCard(mcId, new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                findViewById(R.id.act_my_card_rl).setVisibility(View.GONE);
                ((Button) findViewById(R.id.act_my_card_btn)).setText(R.string.wallet_25);
                findViewById(R.id.act_my_card_btn).setOnClickListener(v -> ARouteHelper.wallet_card_edit().navigation());
                FQT.showShort(MyCardActivity.this, getString(R.string.wallet_19));
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(MyCardActivity.this, msg);
            }
        });
    }
}
