package com.batterbox.power.phone.app.act.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.CardEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.baselib.widget.wheelview.DatePickerDialog;

import java.util.Calendar;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import qiu.niorgai.StatusBarCompat;


/**
 * Created by ass on 2019-08-03.
 * Description
 */

@Route(path = ARouteHelper.WALLET_CARD_EDIT)
public class CardEditActivity extends NavigationActivity {
    public static final int MY_SCAN_REQUEST_CODE = 10;
    //    TextView visaTv, masterTv;
    EditText cardEt, nameEt, monthEt, yearEt, cvvEt;
    String cardType = "VISA";//卡类型：CB，VISA，MC（必须填其中一种）
    String month = "";
    String year = "";

    @Override
    protected int getLayoutId() {
        return R.layout.act_card_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        setNavigationVisible(false);
        LinearLayout mainLay = findViewById(R.id.act_card_edit_main_ll);
        View view = getLayoutInflater().inflate(R.layout.lay_sw_nav_bar, null);
        mainLay.addView(view, 0);
        view.findViewById(R.id.lay_sw_nav_bar_back_btn).setOnClickListener(v1 -> finish());
        ((TextView) view.findViewById(R.id.lay_sw_nav_bar_tv)).setText(R.string.wallet_25);
        view.findViewById(R.id.lay_sw_nav_bar_sub_tv).setVisibility(View.GONE);

//        visaTv = findViewById(R.id.act_card_edit_visa_tv);
//        masterTv = findViewById(R.id.act_card_edit_master_tv);
        cardEt = findViewById(R.id.act_card_edit_card_et);
        nameEt = findViewById(R.id.act_card_edit_user_name_et);
        monthEt = findViewById(R.id.act_card_edit_date_month_et);
//        yearEt = findViewById(R.id.act_card_edit_date_year_et);
        cvvEt = findViewById(R.id.act_card_edit_cvv_et);
//        visaTv.setOnClickListener(v -> {
//            cardType = "VISA";
//            visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, R.mipmap.ic_choced, 0);
//            masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, R.mipmap.ic_no_chose, 0);
//        });
//        masterTv.setOnClickListener(v -> {
//            cardType = "MC";
//            visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, R.mipmap.ic_no_chose, 0);
//            masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, R.mipmap.ic_choced, 0);
//        });
        findViewById(R.id.act_card_edit_date_month_v).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cardEt.setEnabled(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //获取当前日期
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(CardEditActivity.this, DatePickerDialog.FORMAT_MONTH, year, year + 200);
                    datePickerDialog.setOnSelectTimeListener((time, longTime, format) -> {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(longTime);
                        int t = calendar1.get(Calendar.YEAR) / 10 % 10;
                        int s = calendar1.get(Calendar.YEAR) % 10;
                        CardEditActivity.this.year = t + "" + s;
                        CardEditActivity.this.month = StringUtil.fillNumZero(calendar1.get(Calendar.MONTH) + 1);
                        monthEt.setText(StringUtil.fixNullStr(CardEditActivity.this.month) + " / " + StringUtil.fixNullStr(CardEditActivity.this.year));
                    });
                    datePickerDialog.show();
                }
                return true;
            }
        });
        findViewById(R.id.act_card_edit_submit_btn).setOnClickListener(v -> postData());
        findViewById(R.id.act_card_edit_scan_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(CardEditActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false
                        .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, LanguageUtil.getLanguage())
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
                        .putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)//去除水印
                        .putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);//去除键盘
                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });
        cardEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("4")) {
                    cardType = "VISA";
                    cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, R.mipmap.ic_card_visa, 0);
                } else if (s.toString().startsWith("5")) {
                    cardType = "MC";
                    cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, R.mipmap.ic_card_master, 0);
                } else {
                    cardType = "";
                    cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, 0, 0);
                }
            }
        });
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        HttpClient.getInstance().us_queryCard(new NormalHttpCallBack<ResponseEntity<CardEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<CardEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null && !StringUtil.isEmpty(responseEntity.getData().cardNum)) {
                    CardEntity cardEntity = responseEntity.getData();
                    cardEt.setEnabled(false);
                    nameEt.setEnabled(false);
                    yearEt.setEnabled(false);
                    monthEt.setEnabled(false);
                    cvvEt.setEnabled(false);
                    findViewById(R.id.act_card_edit_scan_iv).setVisibility(View.GONE);
                    if (StringUtil.isEquals("VISA", cardEntity.cardType)) {
                        cardType = "VISA";
                        cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, R.mipmap.ic_card_visa, 0);
                    } else if (StringUtil.isEquals("MC", cardEntity.cardType)) {
                        cardType = "MC";
                        cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, R.mipmap.ic_card_master, 0);
                    }
                    cardEt.setText(StringUtil.fixNullStr(cardEntity.cardNum));
                    nameEt.setText(StringUtil.fixNullStr(cardEntity.cardUserName));
                    if (cardEntity.cardExpirationData != null && cardEntity.cardExpirationData.length() >= 4) {
                        year = cardEntity.cardExpirationData.substring(0, 2);
                        month = cardEntity.cardExpirationData.substring(3, 4);
                        monthEt.setText(StringUtil.fixNullStr(month) + " / " + StringUtil.fixNullStr(year));
//                        yearEt.setText(StringUtil.fixNullStr(year));
                    }
                    cvvEt.setText(StringUtil.fixNullStr(cardEntity.cvv));
                    ((Button) findViewById(R.id.act_card_edit_submit_btn)).setText(R.string.wallet_17);
                    findViewById(R.id.act_card_edit_submit_btn).setOnClickListener(v -> {
                        DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.wallet_18), "cancel", v1 -> {

                        }, "ok", v12 -> {
                            del(cardEntity.mcId);
                        });
                    });
                } else {

                }
            }

            @Override
            public void onFail(ResponseEntity<CardEntity> responseEntity, String msg) {
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
                findViewById(R.id.act_card_edit_scan_iv).setVisibility(View.VISIBLE);
                cardEt.setEnabled(true);
                cardEt.setText("");
                nameEt.setEnabled(true);
                nameEt.setText("");
//                yearEt.setEnabled(true);
//                yearEt.setText("");
                monthEt.setEnabled(true);
                monthEt.setText("");
                cvvEt.setEnabled(true);
                cvvEt.setText("");
                findViewById(R.id.act_card_edit_scan_iv).setVisibility(View.VISIBLE);
                cardType = "";
                cardEt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_v, 0, 0, 0);
                findViewById(R.id.act_card_edit_submit_btn).setOnClickListener(v -> postData());
                FQT.showShort(CardEditActivity.this, getString(R.string.wallet_19));
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(CardEditActivity.this, msg);
            }
        });
    }

    private void postData() {
        if (cardEt.getText().length() == 0) {
//            FQT.showShort(this, cardEt.getHint().toString());
            ((TextInputLayout) findViewById(R.id.act_card_edit_card_etil)).setErrorEnabled(true);
            ((TextInputLayout) findViewById(R.id.act_card_edit_card_etil)).setError(getString(R.string.wallet_9));
            return;
        }
        if (nameEt.getText().length() == 0) {
//            FQT.showShort(this, nameEt.getHint().toString());
            ((TextInputLayout) findViewById(R.id.act_card_edit_user_name_etil)).setErrorEnabled(true);
            ((TextInputLayout) findViewById(R.id.act_card_edit_user_name_etil)).setError(getString(R.string.wallet_11));
            return;
        }
        if (monthEt.getText().length() == 0) {
//            FQT.showShort(this, getString(R.string.wallet_16));
            ((TextInputLayout) findViewById(R.id.act_card_edit_date_month_etil)).setErrorEnabled(true);
            ((TextInputLayout) findViewById(R.id.act_card_edit_date_month_etil)).setError(getString(R.string.wallet_16));
            return;
        }
        if (cvvEt.getText().length() == 0) {
            FQT.showShort(this, cvvEt.getHint().toString());
            return;
        }
        //TODO

        HttpClient.getInstance().us_tieCard(cardEt.getText().toString(), nameEt.getText().toString(), year + month, cvvEt.getText().toString(), cardType, new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                if (responseEntity != null) {
                    FQT.showShort(CardEditActivity.this, getString(R.string.app_20));
                }
                finish();
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(CardEditActivity.this, msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
//            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                //resultDisplayStr = "银行卡号: " + scanResult.getRedactedCardNumber() + "\n"; //只显示尾号
//                resultDisplayStr = "银行卡号: " + scanResult.getFormattedCardNumber() + "\n"; //显示银行卡号

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
//                    resultDisplayStr += "有效期：" + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

//                if (scanResult.cvv != null) {
//                    // Never log or display a CVV
//                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
//                }

//                if (scanResult.postalCode != null) {
//                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
//                }
                cardEt.setText(StringUtil.fixNullStr(scanResult.getFormattedCardNumber()).replace(" ", ""));
                cvvEt.setText(StringUtil.fixNullStr(scanResult.cvv));

                if (String.valueOf(scanResult.expiryYear).length() == 4) {
                    int t = scanResult.expiryYear / 10 % 10;
                    int s = scanResult.expiryYear % 10;
                    yearEt.setText(t + String.valueOf(s));
                    monthEt.setText(StringUtil.fillNumZero(scanResult.expiryMonth) + " / " + t + s);
                }

            } else {
//                resultDisplayStr = "Scan was canceled.";
            }

//            mNumberTv.setText(resultDisplayStr);
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
    }
}
