package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.utils.sys.KeyBoardUtil;

/**
 * Created by ass on 2019-10-09.
 * Description
 */
@Route(path = ARouteHelper.VERCODE)
public class VerCodeActivity extends NavigationActivity {
    @Autowired
    public String phone;
    EditText et1, et2, et3, et4;

    @Override
    protected int getLayoutId() {
        return R.layout.act_ver_code;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.login_10);
        et1 = findViewById(R.id.act_ver_code_et1);
        et2 = findViewById(R.id.act_ver_code_et2);
        et3 = findViewById(R.id.act_ver_code_et3);
        et4 = findViewById(R.id.act_ver_code_et4);
        et1.addTextChangedListener(new VerTextWatcher(et1));
        et2.addTextChangedListener(new VerTextWatcher(et2));
        et3.addTextChangedListener(new VerTextWatcher(et3));
        et4.addTextChangedListener(new VerTextWatcher(et4));
        et2.setOnKeyListener(new VerOnKeyListener(et2));
        et3.setOnKeyListener(new VerOnKeyListener(et3));
        et4.setOnKeyListener(new VerOnKeyListener(et4));
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        KeyBoardUtil.showKeyBoardState(this, et1);
    }

    class VerOnKeyListener implements View.OnKeyListener {
        public EditText et;

        public VerOnKeyListener(EditText et) {
            this.et = et;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                String content = et.getText().toString();
                int length = content.length();
                if (length == 0) {
                    if (et.getId() == R.id.act_ver_code_et2) {
                        et1.requestFocus();
                        et1.setSelection(et1.getText().length());
                    } else if (et.getId() == R.id.act_ver_code_et3) {
                        et2.requestFocus();
                        et2.setSelection(et2.getText().length());
                    } else if (et.getId() == R.id.act_ver_code_et4) {
                        et3.requestFocus();
                        et3.setSelection(et3.getText().length());
                    }
                }
            }
            return false;
        }
    }

    class VerTextWatcher implements TextWatcher {
        public EditText et;

        public VerTextWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) return;
            if (et.getId() == R.id.act_ver_code_et1) {
                et2.requestFocus();
                et2.setSelection(et2.getText().length());
            } else if (et.getId() == R.id.act_ver_code_et2) {
                et3.requestFocus();
                et3.setSelection(et3.getText().length());
            } else if (et.getId() == R.id.act_ver_code_et3) {
                et4.requestFocus();
                et4.setSelection(et4.getText().length());
            } else if (et.getId() == R.id.act_ver_code_et4) {
                //
                HttpClient.getInstance().codeIsCorrect(phone, et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString(), new NormalHttpCallBack<ResponseEntity>(VerCodeActivity.this) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        Intent intent = new Intent();
                        intent.putExtra("verCode", et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(VerCodeActivity.this, msg);
                    }
                });
            }
        }
    }
}
