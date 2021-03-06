package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.MainActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.AreaCodeEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.DomainHelper;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.ImUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
@Route(path = ARouteHelper.REGISTER)
public class RegisterActivity extends NavigationActivity {
    EditText phoneEt, pwdEt, pwdEt2, emailEt;//, codeEt
    TextView areaCodeTv;
    TextView codeTv;
    ImageView checkIv;
    String areaCode = "34";
    boolean isCheckAgreement = true;
    //    Disposable disposable;
    String verCode = "";

    @Override
    protected int getLayoutId() {
        return R.layout.act_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        setNavigationVisible(false);
        LinearLayout mainLay = findViewById(R.id.act_register_main_ly);
        View view = getLayoutInflater().inflate(R.layout.lay_sw_nav_bar, null);
        mainLay.addView(view, 0);
        view.findViewById(R.id.lay_sw_nav_bar_back_btn).setOnClickListener(v1 -> finish());
        ((TextView) view.findViewById(R.id.lay_sw_nav_bar_tv)).setText(R.string.login_14);
        view.findViewById(R.id.lay_sw_nav_bar_sub_tv).setVisibility(View.GONE);

        areaCodeTv = findViewById(R.id.act_register_area_tv);
        areaCodeTv.setText("+" + areaCode);
        phoneEt = findViewById(R.id.act_register_phone_et);
        pwdEt = findViewById(R.id.act_register_pwd_et);
        pwdEt2 = findViewById(R.id.act_register_pwd_et2);
//        codeEt = findViewById(R.id.act_register_code_et);
        emailEt = findViewById(R.id.act_register_email_et);
        checkIv = findViewById(R.id.act_register_check_iv);
        codeTv = findViewById(R.id.act_register_code_tv);
        codeTv.setOnClickListener(v -> getVerCode());
        areaCodeTv.setOnClickListener(v -> ARouteHelper.area_code().navigation(RegisterActivity.this, 333));
        checkIv.setOnClickListener(v -> {
            isCheckAgreement = !isCheckAgreement;
            if (isCheckAgreement) {
                checkIv.setImageResource(R.mipmap.ic_choose);
            } else {
                checkIv.setImageResource(R.mipmap.ic_un_choose);
            }
        });
        findViewById(R.id.act_register_submit_btn).setOnClickListener(v -> submit());
//        findViewById(R.id.act_register_user_proc_tv).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getSpecificationH5(), getString(R.string.login_16)).navigation());
//        findViewById(R.id.act_register_proc_tv).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getAgreementH5(), getString(R.string.login_32)).navigation());
        TextView pTv = findViewById(R.id.act_register_register_tv);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString s = new SpannableString(getString(R.string.login_15));
        sb.append(s);
        sb.append(" ");
        SpannableString s1 = new SpannableString(getString(R.string.login_16));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ARouteHelper.hybrid_nav(DomainHelper.getSpecificationH5(), getString(R.string.login_16)).navigation();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        s1.setSpan(clickableSpan, 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ca7800"));
        s1.setSpan(foregroundColorSpan, 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(s1);
        sb.append(" ");
        sb.append(new SpannableString(getString(R.string.login_33)));
        sb.append(" ");
        SpannableString s2 = new SpannableString(getString(R.string.login_32));
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ARouteHelper.hybrid_nav(DomainHelper.getAgreementH5(), getString(R.string.login_32)).navigation();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        s2.setSpan(clickableSpan2, 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.parseColor("#ca7800"));
        s2.setSpan(foregroundColorSpan2, 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(s2);
        //?????????TextView
        pTv.setMovementMethod(LinkMovementMethod.getInstance());
        pTv.setText(sb);

        findViewById(R.id.act_register_pwd_hide_iv).setOnClickListener(v -> {
            if (v.getTag() != null && StringUtil.isEquals(v.getTag(), "show")) {
                v.setTag("hide");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                ((ImageView) findViewById(R.id.act_register_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_hide);
                ViewUtil.setEditCursorLast(pwdEt);
            } else {
                v.setTag("show");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) findViewById(R.id.act_register_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_show);
                ViewUtil.setEditCursorLast(pwdEt);
            }
        });
        findViewById(R.id.act_register_pwd_hide_iv2).setOnClickListener(v -> {
            if (v.getTag() != null && StringUtil.isEquals(v.getTag(), "show")) {
                v.setTag("hide");
                pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                ((ImageView) findViewById(R.id.act_register_pwd_hide_iv2)).setImageResource(R.mipmap.ic_pwd_hide);
                ViewUtil.setEditCursorLast(pwdEt2);
            } else {
                v.setTag("show");
                pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) findViewById(R.id.act_register_pwd_hide_iv2)).setImageResource(R.mipmap.ic_pwd_show);
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
                register();
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
        if (!isCheckAgreement) {
            FQT.showShort(this, getString(R.string.login_18));
            return;
        }
        HttpClient.getInstance().findMemExistByPhoneAndArea(areaCode, phoneEt.getText().toString(), new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                getVerCode();
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(RegisterActivity.this, msg);
            }
        });
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
//                FQT.showShort(RegisterActivity.this, getString(R.string.login_20));
//                startCountDown();
                ARouteHelper.vercode(phoneEt.getText().toString()).navigation(RegisterActivity.this, 444);
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(RegisterActivity.this, msg);
            }
        });

    }

//    private void startCountDown() {
//        disposable = CountDownHelper.buildCountDown(60, new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                FQL.d("?????????", "??????");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                FQL.d("?????????", "aLong=" + aLong);
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
//                FQL.d("?????????", "??????");
//                codeTv.setEnabled(true);
//                codeTv.setText(R.string.login_13);
//            }
//
//            @Override
//            public void onComplete() {
//                FQL.d("?????????", "??????");
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

    private void register() {
        HttpClient.getInstance().register(areaCode, phoneEt.getText().toString(), verCode, pwdEt.getText().toString(), emailEt.getText().toString(), new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                FQT.showShort(RegisterActivity.this, getString(R.string.login_19));
                login(areaCode, phoneEt.getText().toString(), pwdEt.getText().toString());
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(RegisterActivity.this, msg);
            }
        });
    }

    private void login(String areaCode, String phone, String pwd) {
        HttpClient.getInstance().login(areaCode, phone, pwd, new NormalHttpCallBack<ResponseEntity<UserEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<UserEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    UserUtil.saveToken(responseEntity.getData().token);
                    UserUtil.saveUserInfo(responseEntity.getData());
                    ImUtil.login();
                    MainActivity.goToMain(RegisterActivity.this, true);
                    finish();
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {
                FQT.showShort(RegisterActivity.this, msg);
            }
        });
    }
}
