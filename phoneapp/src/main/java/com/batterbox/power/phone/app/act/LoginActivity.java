package com.batterbox.power.phone.app.act;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.BuildConfig;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.MainActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.AreaCodeEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.event.ExitEvent;
import com.batterbox.power.phone.app.http.DomainHelper;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.hybrid.IJavaScriptInterface;
import com.batterbox.power.phone.app.utils.ImUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.SharedPreferencesUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.print.FQT;

import org.greenrobot.eventbus.EventBus;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
@Route(path = ARouteHelper.LOGIN)
public class LoginActivity extends NavigationActivity implements IJavaScriptInterface {
    EditText phoneEt, pwdEt;

    TextView areaCodeTv, rememberTv;
    String areaCode = "34";
    boolean isRemember = false;
    ImageView checkIv;
    boolean isCheckAgreement = true;

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        setNavigationVisible(false);
        LinearLayout mainLay = findViewById(R.id.act_login_main_ly);
        View view = getLayoutInflater().inflate(R.layout.lay_sw_nav_bar, null);
        mainLay.addView(view, 0);
        view.findViewById(R.id.lay_sw_nav_bar_back_btn).setOnClickListener(v1 -> finish());
        ((TextView) view.findViewById(R.id.lay_sw_nav_bar_tv)).setText(R.string.login_1);
        view.findViewById(R.id.lay_sw_nav_bar_sub_tv).setVisibility(View.GONE);

        checkIv = findViewById(R.id.act_login_check_iv);
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
        findViewById(R.id.act_login_pwd_hide_iv).setOnClickListener(v -> {
            if (v.getTag() != null && StringUtil.isEquals(v.getTag(), "show")) {
                v.setTag("hide");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                ((ImageView) findViewById(R.id.act_login_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_hide);
                ViewUtil.setEditCursorLast(pwdEt);
            } else {
                v.setTag("show");
                pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) findViewById(R.id.act_login_pwd_hide_iv)).setImageResource(R.mipmap.ic_pwd_show);
                ViewUtil.setEditCursorLast(pwdEt);
            }
        });

        checkIv.setOnClickListener(v -> {
            isCheckAgreement = !isCheckAgreement;
            if (isCheckAgreement) {
                checkIv.setImageResource(R.mipmap.ic_choose);
            } else {
                checkIv.setImageResource(R.mipmap.ic_un_choose);
            }
        });
        TextView pTv = findViewById(R.id.act_login_register_check_tv);
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
        //配置给TextView
        pTv.setMovementMethod(LinkMovementMethod.getInstance());
        pTv.setText(sb);

        if (!SharedPreferencesUtil.getInstance().getBoolean(BuildConfig.VERSION_NAME + "PermissShow")) {
            showPermissDialog2();

        }
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
                    ImUtil.login();
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
                    MainActivity.goToMain(LoginActivity.this, true);
                    finish();
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {
                FQT.showShort(LoginActivity.this, msg);
            }
        });
    }


    private void showPermissDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alert_dialog);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.dialog_premiss, null);
        TextView pTv = view.findViewById(R.id.p_tv);
//        TextView pUserTv = view.findViewById(R.id.p_user_tv);
//        TextView pMTv = view.findViewById(R.id.p_m_tv);
        TextView disagreeBtn = view.findViewById(R.id.disagree_btn);
        TextView agreedBtn = view.findViewById(R.id.agree_btn);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
//        alertDialog.getWindow().setWindowAnimations(R.style.alert_dialog_animation);
        alertDialog.getWindow().setLayout(ViewUtil.dip2px(this, 318), LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.setCancelable(false);
        disagreeBtn.setOnClickListener(view14 -> alertDialog.dismiss());
        agreedBtn.setOnClickListener(view13 -> alertDialog.dismiss());
//        pUserTv.setOnClickListener(view12 -> ARouteHelper.hybrid_nav(DomainHelper.getSpecificationH5(), getString(R.string.setting_3)).navigation());
//        pMTv.setOnClickListener(view1 -> ARouteHelper.hybrid_nav(DomainHelper.getAgreementH5(), getString(R.string.setting_4)).navigation());


        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString s = new SpannableString(getString(R.string.p_alert_6));
        sb.append(s);
        sb.append(" ");
        SpannableString s1 = new SpannableString(getString(R.string.p_alert_7));
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
        sb.append(new SpannableString(getString(R.string.p_alert_8)));
        sb.append(" ");
        SpannableString s2 = new SpannableString(getString(R.string.p_alert_9));
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
        //配置给TextView
        pTv.setMovementMethod(LinkMovementMethod.getInstance());
        pTv.setText(sb);

    }

    @SuppressLint("JavascriptInterface")
    private void showPermissDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alert_dialog);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.dialog_premiss2, null);
        WebView webView = view.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, IJavaScriptInterface.JsObjectName);
//        TextView pUserTv = view.findViewById(R.id.p_user_tv);
//        TextView pMTv = view.findViewById(R.id.p_m_tv);
        TextView disagreeBtn = view.findViewById(R.id.disagree_btn);
        TextView agreedBtn = view.findViewById(R.id.agree_btn);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
//        alertDialog.getWindow().setWindowAnimations(R.style.alert_dialog_animation);
        alertDialog.getWindow().setLayout(ViewUtil.dip2px(this, 318), LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.setCancelable(false);
        disagreeBtn.setOnClickListener(view14 -> {
            alertDialog.dismiss();
            EventBus.getDefault().post(new ExitEvent());
            finish();
        });
        agreedBtn.setOnClickListener(view13 -> {
            SharedPreferencesUtil.getInstance().saveBoolean(BuildConfig.VERSION_NAME + "PermissShow", true);
            alertDialog.dismiss();
        });

        webView.loadUrl("https://app.battery2box.com/agreement2.html");

    }

    @JavascriptInterface
    public String getLanguage() {
        FQL.d("JavascriptInterface getLanguage");
        String language = LanguageUtil.getLanguage();
        if (StringUtil.isEmpty(language)) {
            language = LanguageUtil.ZH;
        }
        return language;
    }

    @JavascriptInterface
    public void save_img64(String imgBase64) {
    }


    @JavascriptInterface
    public void shopkeeper_log(String log) {
    }


    @JavascriptInterface
    public String shopkeeper_getToken() {
        return StringUtil.fixNullStr(UserUtil.getToken());
    }


    @JavascriptInterface
    public void shopkeeper_closeWindow() {
    }


    @JavascriptInterface
    public void shopkeeper_share_file(String json) {
    }

    @JavascriptInterface
    public void shopkeeper_share_link(String json) {
    }

    @JavascriptInterface
    public void shopkeeper_down_file(String json) {
    }

    @JavascriptInterface
    public void shopkeeper_share_imgs(String json) {

    }

    @JavascriptInterface
    public void shopkeeper_user_agreement_agree() {
    }

    @JavascriptInterface
    public void shopkeeper_user_agreement_refuse() {
    }

}
