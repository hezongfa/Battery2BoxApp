package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-08-13.
 * Description
 */
@Route(path = ARouteHelper.COOPERATION)
public class MerchantsCooperationActivity extends NavigationActivity {
    //1=装机合作，2=广告合作，3=其它
    int type = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.act_cooperation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_7);
        findViewById(R.id.act_cooperation_submit_btn).setOnClickListener(v -> postData());
        findViewById(R.id.act_cooperation_type_tv1).setOnClickListener(v -> {
            type = 1;
            switchType();
        });
        findViewById(R.id.act_cooperation_type_tv2).setOnClickListener(v -> {
            type = 2;
            switchType();
        });
        findViewById(R.id.act_cooperation_type_tv3).setOnClickListener(v -> {
            type = 3;
            switchType();
        });
        switchType();
    }

    private void switchType() {
        switch (type) {
            case 1:
                findViewById(R.id.act_cooperation_type_tv1).setSelected(true);
                findViewById(R.id.act_cooperation_type_tv2).setSelected(false);
                findViewById(R.id.act_cooperation_type_tv3).setSelected(false);
                findViewById(R.id.act_cooperation_remark_et).setVisibility(View.GONE);
                break;
            case 2:
                findViewById(R.id.act_cooperation_type_tv1).setSelected(false);
                findViewById(R.id.act_cooperation_type_tv2).setSelected(true);
                findViewById(R.id.act_cooperation_type_tv3).setSelected(false);
                findViewById(R.id.act_cooperation_remark_et).setVisibility(View.GONE);
                break;
            case 3:
                findViewById(R.id.act_cooperation_type_tv1).setSelected(false);
                findViewById(R.id.act_cooperation_type_tv2).setSelected(false);
                findViewById(R.id.act_cooperation_type_tv3).setSelected(true);
                findViewById(R.id.act_cooperation_remark_et).setVisibility(View.VISIBLE);
                break;
        }
    }

    private String getTextById(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }

    private void postData() {
        HttpClient.getInstance().api_bs_businCoo(type, getTextById(R.id.act_cooperation_email_et), getTextById(R.id.act_cooperation_business_name_et),
                getTextById(R.id.act_cooperation_name_et), getTextById(R.id.act_cooperation_phone_et), getTextById(R.id.act_cooperation_remark_et), new NormalHttpCallBack<ResponseEntity>(this) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        FQT.showShort(MerchantsCooperationActivity.this, StringUtil.fixNullStr(responseEntity.getMsg(), getString(R.string.app_20)));
                        finish();
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(MerchantsCooperationActivity.this, msg);
                    }
                });
    }
}
