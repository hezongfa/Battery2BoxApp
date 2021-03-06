package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.BorrowResultEntity;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.OrderStateEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CountDownHelper;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.dialog.DialogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ass on 2019-07-31.
 * Description
 */
@Route(path = ARouteHelper.BORROW_RESULT)
public class BorrowResultActivity extends NavigationActivity {
    @Autowired
    public BorrowResultEntity borrowResultEntity;
    @Autowired
    public DeviceEntity deviceEntity;
    Disposable disposable;
    ProgressBar progressBar;
    TextView progressTv;

    @Override
    protected int getLayoutId() {
        return R.layout.act_borrow_result;
    }

    long tempTime = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressTv = findViewById(R.id.act_borrow_result_tv);
        backTv.setOnClickListener(v -> exit());
        progressBar = findViewById(R.id.act_borrow_result_pb);
        if (borrowResultEntity == null) {
            finish();
        }
        progressBar.setMax(1000 * 60);
        disposable = CountDownHelper.buildCountDown(1000 * 60, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                FQL.d("?????????", "??????");
            }

            @Override
            public void onNext(Long aLong) {
                if (aLong == 0) {
                    DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.borrow_15), getString(R.string.app_48), v -> finish()).setCancelable(false);
                    return;
                }
                FQL.d("?????????", "aLong=" + aLong);
                int progress = (int) (1000 * 60 - aLong);
                progressBar.setProgress(progress);
                progressTv.setText(progress / 600 + "%");
                if (tempTime == -1) {
                    tempTime = System.currentTimeMillis();
                }
                long cur = System.currentTimeMillis();
                if (cur - tempTime > 8000) {
                    getState();
                    tempTime = cur;
                    FQL.d("?????????", "--------tempTime==" + tempTime);
                }
            }

            @Override
            public void onError(Throwable e) {
                FQL.d("?????????", "??????");
                finish();
            }

            @Override
            public void onComplete() {
                FQL.d("?????????", "??????");
                progressBar.setProgress(progressBar.getMax());
                finish();
            }
        }, 1, TimeUnit.MILLISECONDS);
        ImageView iv = findViewById(R.id.act_borrow_result_iv);
        iv.setVisibility(View.VISIBLE);
        findViewById(R.id.act_borrow_result_tip_tv).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.act_borrow_result_num_tv)).setText(borrowResultEntity.btBay + "");
        switch (borrowResultEntity.btBay) {
            case 1:
                iv.setImageResource(R.mipmap.pic_borrow_1);
                break;
            case 2:
                iv.setImageResource(R.mipmap.pic_borrow_2);
                break;
            case 3:
                iv.setImageResource(R.mipmap.pic_borrow_3);
                break;
            case 4:
                iv.setImageResource(R.mipmap.pic_borrow_4);
                break;
            case 5:
                iv.setImageResource(R.mipmap.pic_borrow_5);
                break;
            default:
                iv.setVisibility(View.GONE);
                findViewById(R.id.act_borrow_result_tip_tv).setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) CountDownHelper.cancelCountDown(disposable);
        super.onDestroy();
    }

    private void getState() {
        HttpClient.getInstance().order_state(borrowResultEntity.orderCode, new NormalHttpCallBack<ResponseEntity<OrderStateEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<OrderStateEntity> responseEntity) {
                if (responseEntity.getData() != null) {
                    if (responseEntity.getData().state == 1) {
                        if (disposable != null) CountDownHelper.cancelCountDown(disposable);
                        //??????????????????0???= ???????????????1???= ???????????????2???= ??????????????????3???= ????????????
                        ARouteHelper.borrow_success(responseEntity.getData(),deviceEntity).navigation();
                        finish();
                    }
                }
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(BorrowResultActivity.this, msg);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        FQT.showShort(this, "???????????????");
    }
}
