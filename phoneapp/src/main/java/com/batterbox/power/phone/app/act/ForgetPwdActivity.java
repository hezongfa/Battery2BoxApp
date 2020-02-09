package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.AreaCodeEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
@Route(path = ARouteHelper.FORGETPWD)
public class ForgetPwdActivity extends NavigationActivity {
    EditText phoneEt, pwdEt, pwdEt2;//, codeEt
    TextView areaCodeTv;
    TextView codeTv;
    String areaCode = "34";
//    Disposable disposable;
    String verCode = "";

    @Override
    protected int getLayoutId() {
        return R.layout.act_editpwd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.login_7);
        areaCodeTv = findViewById(R.id.act_editpwd_area_tv);
        areaCodeTv.setText("+" + areaCode);
        phoneEt = findViewById(R.id.act_editpwd_phone_et);
        pwdEt = findViewById(R.id.act_editpwd_pwd_et);
        pwdEt2 = findViewById(R.id.act_editpwd_pwd_et2);
//        codeEt = findViewById(R.id.act_editpwd_code_et);
        codeTv = findViewById(R.id.act_editpwd_code_tv);
        codeTv.setOnClickListener(v -> getVerCode());
        areaCodeTv.setOnClickListener(v -> ARouteHelper.area_code().navigation(ForgetPwdActivity.this, 333));
        findViewById(R.id.act_editpwd_submit_btn).setOnClickListener(v -> submit());
        findViewById(R.id.act_editpwd_pwd_hide_iv).setOnClickListener(v -> {
            if (v.getTag() != null && StringUtil.isEquals(v.getTag(), "show")) {
                v.setTag("hide");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                ((ImageView) findViewById(R.id.act_editpwd_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_hide);
                ViewUtil.setEditCursorLast(pwdEt);
            } else {
                v.setTag("show");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) findViewById(R.id.act_editpwd_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_show);
                ViewUtil.setEditCursorLast(pwdEt);
            }
        });
        findViewById(R.id.act_editpwd_pwd_hide_iv2).setOnClickListener(v -> {
            if (v.getTag() != null && StringUtil.isEquals(v.getTag(), "show")) {
                v.setTag("hide");
                pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                ((ImageView) findViewById(R.id.act_editpwd_pwd_hide_iv2)).setImageResource(R.mipmap.ic_pwd_hide);
                ViewUtil.setEditCursorLast(pwdEt2);
            } else {
                v.setTag("show");
                pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) findViewById(R.id.act_editpwd_pwd_hide_iv2)).setImageResource(R.mipmap.ic_pwd_show);
                ViewUtil.setEditCursorLast(pwdEt2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 333) {
                AreaCodeEntity areaCodeEntity = (AreaCodeEntity) data.getSerializableExtra("areaCodeEntity");
                if (areaCodeEntity != null) {
                    areaCodeTv.setText("+" + (areaCode = StringUtil.fixNullStr(areaCodeEntity.mobilePrefix)));
                }
            } else if (requestCode == 444) {
                verCode = data.getStringExtra("verCode");
                editPwd();
            }
        }
    }

    private void submit() {
        if (phoneEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.login_3));
            return;
        }
//        if (codeEt.getText().length() == 0) {
//            FQT.showShort(this, getString(R.string.login_12));
//            return;
//        }
        if (pwdEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.login_4));
            return;
        }
        if (!StringUtil.isEquals(pwdEt2.getText().toString(), pwdEt.getText().toString())) {
            FQT.showShort(this, getString(R.string.login_17));
            return;
        }
        getVerCode();

    }

    private void getVerCode() {
        if (phoneEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.login_3));
            return;
        }
        HttpClient.getInstance().getVerCode(areaCode, phoneEt.getText().toString(), new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
//                FQT.showShort(ForgetPwdActivity.this, getString(R.string.login_20));
//                startCountDown();
                ARouteHelper.vercode(phoneEt.getText().toString()).navigation(ForgetPwdActivity.this, 444);
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(ForgetPwdActivity.this, msg);
            }
        });

    }

//    private void startCountDown() {
//        disposable = CountDownHelper.buildCountDown(60, new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                FQL.d("倒计时", "开始");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                FQL.d("倒计时", "aLong=" + aLong);
//                if (aLong > 0) {
//                    codeTv.setEnabled(false);
//                    codeTv.setText(aLong + "s");
//                } else {
//                    codeTv.setEnabled(true);
//                    codeTv.setText(R.string.login_13);
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                FQL.d("倒计时", "异常");
//                codeTv.setEnabled(true);
//                codeTv.setText(R.string.login_13);
//            }
//
//            @Override
//            public void onComplete() {
//                FQL.d("倒计时", "完毕");
//                codeTv.setEnabled(true);
//                codeTv.setText(R.string.login_13);
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
//        if (disposable != null) CountDownHelper.cancelCountDown(disposable);
        super.onDestroy();
    }

    private void editPwd() {
        HttpClient.getInstance().editPwd(areaCode, phoneEt.getText().toString(), verCode, pwdEt.getText().toString(), new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                FQT.showShort(ForgetPwdActivity.this, getString(R.string.login_31));
                finish();
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(ForgetPwdActivity.this, msg);
            }
        });
    }
}
