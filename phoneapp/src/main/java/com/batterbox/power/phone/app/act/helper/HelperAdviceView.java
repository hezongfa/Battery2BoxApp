package com.batterbox.power.phone.app.act.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.ScanBarCodeHelper;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavTabPagerViewI;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-08-08.
 * Description
 */
public class HelperAdviceView extends LinearLayout implements NavTabPagerViewI {
    String faultCode = "1";
    String qrCode;
    HelperActivity helperActivity;


    public HelperAdviceView(Context context) {
        super(context);
        helperActivity = (HelperActivity) context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_helper_advice, this);
    }

    @Override
    public View getContentView() {
        return this;
    }

    @Override
    public void onCreateView(Context context) {

        findViewById(R.id.view_helper_advice_tv1).setOnClickListener(v -> {
            faultCode = "1";
            findViewById(R.id.view_helper_advice_tv1).setSelected(true);
            ((TextView) findViewById(R.id.view_helper_advice_tv1)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_1, 0, 0);
            findViewById(R.id.view_helper_advice_tv2).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv2)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_2_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv3).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv3)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_3_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv4).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv4)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_4_n, 0, 0);
        });
        findViewById(R.id.view_helper_advice_tv2).setOnClickListener(v -> {
            faultCode = "2";
            findViewById(R.id.view_helper_advice_tv1).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv1)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_1_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv2).setSelected(true);
            ((TextView) findViewById(R.id.view_helper_advice_tv2)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_2, 0, 0);
            findViewById(R.id.view_helper_advice_tv3).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv3)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_3_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv4).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv4)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_4_n, 0, 0);
        });
        findViewById(R.id.view_helper_advice_tv3).setOnClickListener(v -> {
            faultCode = "3";
            findViewById(R.id.view_helper_advice_tv1).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv1)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_1_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv2).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv2)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_2_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv3).setSelected(true);
            ((TextView) findViewById(R.id.view_helper_advice_tv3)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_3, 0, 0);
            findViewById(R.id.view_helper_advice_tv4).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv4)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_4_n, 0, 0);
        });
        findViewById(R.id.view_helper_advice_tv4).setOnClickListener(v -> {
            faultCode = "4";
            findViewById(R.id.view_helper_advice_tv1).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv1)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_1_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv2).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv2)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_2_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv3).setSelected(false);
            ((TextView) findViewById(R.id.view_helper_advice_tv3)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_3_n, 0, 0);
            findViewById(R.id.view_helper_advice_tv4).setSelected(true);
            ((TextView) findViewById(R.id.view_helper_advice_tv4)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_advice_4, 0, 0);
        });
        findViewById(R.id.view_helper_advice_tv1).callOnClick();
        findViewById(R.id.view_helper_advice_qr_iv).setOnClickListener(v -> ScanBarCodeHelper.scan(helperActivity, result -> {
            FQL.e("onScanSuccess: " + result);
            String scanUrl = result.getContent();
            if (!StringUtil.isEmpty(scanUrl)) {
                setScanUrl(scanUrl);
            }
        }));
        findViewById(R.id.view_helper_advice_submit_btn).setOnClickListener(v -> postData());
    }

    private void postData() {
        HttpClient.getInstance().api_bs_deviceFault(faultCode,
                StringUtil.fixNullStr(helperActivity.lat),
                StringUtil.fixNullStr(helperActivity.lng),
                qrCode,
                ((EditText) findViewById(R.id.view_helper_advice_content_et)).getText().toString(),
                ((EditText) findViewById(R.id.view_helper_advice_email_et)).getText().toString(),
                new NormalHttpCallBack<ResponseEntity>(getContext()) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        FQT.showShort(getContext(), getContext().getString(R.string.app_20));
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(getContext(), msg);
                    }
                });
    }

    public void setScanUrl(String scanUrl) {
        EditText qrTv = findViewById(R.id.view_helper_advice_qr_tv);
        if (qrTv != null) {
            qrCode = StringUtil.fixNullStr(scanUrl);
            qrTv.setText(StringUtil.fixNullStr(scanUrl));
        }
    }
}
