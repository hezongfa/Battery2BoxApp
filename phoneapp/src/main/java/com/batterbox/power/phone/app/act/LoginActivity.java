package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.AreaCodeEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.batterbox.power.phone.app.utils.VersionHelper;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.SharedPreferencesUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
@Route(path = ARouteHelper.LOGIN)
public class LoginActivity extends NavigationActivity {
    EditText phoneEt, pwdEt;

    TextView areaCodeTv, rememberTv;
    String areaCode = "34";
    boolean isRemember = false;

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.login_1);
        rememberTv = findViewById(R.id.act_login_remember_tv);
        areaCodeTv = findViewById(R.id.act_login_area_tv);
        areaCodeTv.setText("+" + areaCode);
        areaCodeTv.setOnClickListener(v -> ARouteHelper.area_code().navigation(LoginActivity.this, 333));
        phoneEt = findViewById(R.id.act_login_phone_et);
        pwdEt = findViewById(R.id.act_login_pwd_et);
        findViewById(R.id.act_login_register_tv).setOnClickListener(v -> ARouteHelper.register().navigation());
        findViewById(R.id.act_login_forget_pwd_tv).setOnClickListener(v -> ARouteHelper.forgetpwd().navigation());
        findViewById(R.id.act_login_submit_btn).setOnClickListener(v -> submit());
//        VersionHelper.checkVersion(this);
        if (isRemember = SharedPreferencesUtil.getInstance().getBoolean("isRememberPwd")) {
            areaCode = SharedPreferencesUtil.getInstance().getString("R_areaCode", "34");
            areaCodeTv.setText("+" + areaCode);
            rememberTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_choose, 0, 0, 0);
            phoneEt.setText(SharedPreferencesUtil.getInstance().getString("R_phone", ""));
            pwdEt.setText(SharedPreferencesUtil.getInstance().getString("R_pwd", ""));
        } else {
            rememberTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_un_choose, 0, 0, 0);
        }
        rememberTv.setOnClickListener(v -> {
            isRemember = !isRemember;
            if (isRemember) {
                rememberTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_choose, 0, 0, 0);
            } else {
                rememberTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_un_choose, 0, 0, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == RESULT_OK && data != null) {
            AreaCodeEntity areaCodeEntity = (AreaCodeEntity) data.getSerializableExtra("areaCodeEntity");
            if (areaCodeEntity != null) {
                areaCodeTv.setText("+" + (areaCode = StringUtil.fixNullStr(areaCodeEntity.mobilePrefix)));
            }
        }
    }

    private void submit() {
        if (phoneEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.login_3));
            return;
        }
        if (pwdEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.login_4));
            return;
        }
        HttpClient.getInstance().login(areaCode, phoneEt.getText().toString(), pwdEt.getText().toString(), new NormalHttpCallBack<ResponseEntity<UserEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<UserEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    UserUtil.saveToken(responseEntity.getData().token);
                    UserUtil.saveUserInfo(responseEntity.getData());
                    if (isRemember) {
                        SharedPreferencesUtil.getInstance().saveString("R_areaCode", areaCode);
                        SharedPreferencesUtil.getInstance().saveString("R_phone", phoneEt.getText().toString());
                        SharedPreferencesUtil.getInstance().saveString("R_pwd", pwdEt.getText().toString());
                    } else {
                        SharedPreferencesUtil.getInstance().remove("R_areaCode");
                        SharedPreferencesUtil.getInstance().remove("R_phone");
                        SharedPreferencesUtil.getInstance().remove("R_pwd");
                    }
                    SharedPreferencesUtil.getInstance().saveBoolean("isRememberPwd", isRemember);
                    finish();
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {
                FQT.showShort(LoginActivity.this, msg);
            }
        });
    }
}