package com.batterbox.power.phone.app.act.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


/**
 * Created by ass on 2019-08-03.
 * Description
 */

@Route(path = ARouteHelper.WALLET_CARD_EDIT)
public class CardEditActivity extends NavigationActivity {
    public static final int MY_SCAN_REQUEST_CODE = 10;
    TextView visaTv, masterTv;
    EditText cardEt, nameEt, monthEt, yearEt, cvvEt;
    String cardType = "VISA";//卡类型：CB，VISA，MC（必须填其中一种）

    @Override
    protected int getLayoutId() {
        return R.layout.act_card_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visaTv = findViewById(R.id.act_card_edit_visa_tv);
        masterTv = findViewById(R.id.act_card_edit_master_tv);
        cardEt = findViewById(R.id.act_card_edit_card_et);
        nameEt = findViewById(R.id.act_card_edit_user_name_et);
        monthEt = findViewById(R.id.act_card_edit_date_month_et);
        yearEt = findViewById(R.id.act_card_edit_date_year_et);
        cvvEt = findViewById(R.id.act_card_edit_cvv_et);
        visaTv.setOnClickListener(v -> {
            cardType = "VISA";
            visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, R.mipmap.ic_choced, 0);
            masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, R.mipmap.ic_no_chose, 0);
        });
        masterTv.setOnClickListener(v -> {
            cardType = "MC";
            visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, R.mipmap.ic_no_chose, 0);
            masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, R.mipmap.ic_choced, 0);
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
                    //VISA
                    visaTv.callOnClick();
                } else if (s.toString().startsWith("5")) {
                    masterTv.callOnClick();
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
                findViewById(R.id.act_card_edit_content_ll).setVisibility(View.VISIBLE);
                if (responseEntity != null && responseEntity.getData() != null && !StringUtil.isEmpty(responseEntity.getData().cardNum)) {
                    CardEntity cardEntity = responseEntity.getData();
                    visaTv.setEnabled(false);
                    masterTv.setEnabled(false);
                    cardEt.setEnabled(false);
                    nameEt.setEnabled(false);
                    yearEt.setEnabled(false);
                    monthEt.setEnabled(false);
                    cvvEt.setEnabled(false);
                    findViewById(R.id.act_card_edit_scan_iv).setVisibility(View.GONE);
                    findViewById(R.id.act_card_edit_type_line_v).setVisibility(View.GONE);
                    if (StringUtil.isEquals("VISA", cardEntity.cardType)) {
                        cardType = "VISA";
                        visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, 0, 0);
                        masterTv.setVisibility(View.GONE);
                    } else if (StringUtil.isEquals("MC", cardEntity.cardType)) {
                        cardType = "MC";
                        masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, 0, 0);
                        visaTv.setVisibility(View.GONE);
                    }
                    cardEt.setText(StringUtil.fixNullStr(cardEntity.cardNum));
                    nameEt.setText(StringUtil.fixNullStr(cardEntity.cardUserName));
                    if (cardEntity.cardExpirationData != null && cardEntity.cardExpirationData.length() >= 4) {
                        String year = cardEntity.cardExpirationData.substring(0, 2);
                        String month = cardEntity.cardExpirationData.substring(3, 4);
                        monthEt.setText(StringUtil.fixNullStr(month));
                        yearEt.setText(StringUtil.fixNullStr(year));
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
                findViewById(R.id.act_card_edit_content_ll).setVisibility(View.VISIBLE);
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
                findViewById(R.id.act_card_edit_type_line_v).setVisibility(View.VISIBLE);
                visaTv.setVisibility(View.VISIBLE);
                visaTv.setEnabled(true);
                masterTv.setVisibility(View.VISIBLE);
                masterTv.setEnabled(true);
                cardEt.setEnabled(true);
                cardEt.setText("");
                nameEt.setEnabled(true);
                nameEt.setText("");
                yearEt.setEnabled(true);
                yearEt.setText("");
                monthEt.setEnabled(true);
                monthEt.setText("");
                cvvEt.setEnabled(true);
                cvvEt.setText("");
                findViewById(R.id.act_card_edit_scan_iv).setVisibility(View.VISIBLE);
                cardType = "VISA";
                visaTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_visa, 0, R.mipmap.ic_choced, 0);
                masterTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_card_master, 0, R.mipmap.ic_no_chose, 0);
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
            FQT.showShort(this, cardEt.getHint().toString());
            return;
        }
//        if (nameEt.getText().length() == 0) {
//            FQT.showShort(this, nameEt.getHint().toString());
//            return;
//        }
        if (monthEt.getText().length() == 0 || yearEt.getText().length() == 0) {
            FQT.showShort(this, getString(R.string.wallet_16));
            return;
        }
        if (cvvEt.getText().length() == 0) {
            FQT.showShort(this, cvvEt.getHint().toString());
            return;
        }
        HttpClient.getInstance().us_tieCard(cardEt.getText().toString(), nameEt.getText().toString(), yearEt.getText().toString() + monthEt.getText().toString(), cvvEt.getText().toString(), cardType, new NormalHttpCallBack<ResponseEntity>(this) {
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
                monthEt.setText(StringUtil.fillNumZero(scanResult.expiryMonth));
                if (String.valueOf(scanResult.expiryYear).length() == 4) {
                    int t = scanResult.expiryYear / 10 % 10;
                    int s = scanResult.expiryYear % 10;
                    yearEt.setText(t + String.valueOf(s));
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
