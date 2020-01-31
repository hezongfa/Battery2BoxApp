package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

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
 * Created by ass on 2019-08-17.
 * Description
 */
@Route(path = ARouteHelper.SETTING_CHANGEPWD)
public class ChangePwdActivity extends NavigationActivity {
    EditText oldPwdEt, pwdEt, pwdEt2;

    @Override
    protected int getLayoutId() {
        return R.layout.act_change_pwd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.login_30);
        oldPwdEt = findViewById(R.id.act_change_pwd_pwd_et);
        pwdEt = findViewById(R.id.act_change_pwd_new_pwd_et);
        pwdEt2 = findViewById(R.id.act_change_pwd_pwd_et2);
        findViewById(R.id.act_change_pwd_submit_btn).setOnClickListener(v -> {
            if (oldPwdEt.getText().length() == 0) {
                FQT.showShort(ChangePwdActivity.this, getString(R.string.login_27));
                return;
            }
            if (pwdEt.getText().length() == 0) {
                FQT.showShort(ChangePwdActivity.this, getString(R.string.login_28));
                return;
            }
            if (!StringUtil.isEquals(pwdEt2.getText().toString(), pwdEt.getText().toString())) {
                FQT.showShort(ChangePwdActivity.this, getString(R.string.login_17));
                return;
            }
            HttpClient.getInstance().us_modifyPwd(oldPwdEt.getText().toString(), pwdEt.getText().toString(), new NormalHttpCallBack<ResponseEntity>(ChangePwdActivity.this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity responseEntity) {
                    if (responseEntity != null) {
                        FQT.showShort(ChangePwdActivity.this, StringUtil.fixNullStr(responseEntity.getMsg(), getString(R.string.login_31)));
                    }
                    finish();
                }

                @Override
                public void onFail(ResponseEntity responseEntity, String msg) {
                    FQT.showShort(ChangePwdActivity.this, msg);
                }
            });
        });
    }
}
