package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.batterbox.power.phone.app.AppConfigUtil;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.MainActivity;
import com.chenyi.baselib.ui.BaseActivity;

import java.lang.ref.WeakReference;

public class WelcomeActivity extends BaseActivity {
    private static Handler transHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);
        hideBottomUIMenu();
//        adIv = findViewById(R.id.act_welcome_ad);
//        skinBtn = findViewById(R.id.act_welcome_skip_ad);
//
////        AppConfigUtil.initConfig();
//        skinBtn.setOnClickListener(v -> startMainActivity());

        WeakReference<WelcomeActivity> weakReference = new WeakReference<>(this);
        if (AppConfigUtil.isFirstLaunchByVersion()) {
            AppConfigUtil.setFirstLaunch(false);
            transHandler.postDelayed(() -> {
                if (weakReference.get() != null) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }, 500);
        } else {
            transHandler.postDelayed(() -> {
                if (weakReference.get() != null) {
//                    if (StringUtil.isEmpty(UserUtil.getToken())) {
//                        startActivity(new Intent(this, LoginActivity.class));
//                    } else {
                    startActivity(new Intent(this, MainActivity.class));
////                    }
                    finish();
                }
            }, 500);
        }
    }

//        boolean isFirstLaunch = AppConfigUtil.isFirstLaunch();
//        ArrayList<ConfigFlashAdvEntity> flashAdvEntitieList = FlashAdvUtil.getFlashAdv();
//        ArrayList<ConfigFlashAdvEntity> flashAdvEntities = new ArrayList<>();
//        if (!StringUtil.isEmpty(flashAdvEntitieList)) {
//            long curTime = System.currentTimeMillis() / 1000L;
//            for (ConfigFlashAdvEntity configFlashAdvEntity : flashAdvEntitieList) {
//                if (configFlashAdvEntity.start_time <= curTime && curTime <= configFlashAdvEntity.end_time) {
//                    flashAdvEntities.add(configFlashAdvEntity);
//                }
//            }
//        }
//        if (isFirstLaunch || StringUtil.isEmpty(flashAdvEntities)) {
//            WeakReference<WelcomeActivity> weakReference = new WeakReference<>(this);
//            if (AppConfigUtil.isFirstLaunchByVersion()) {
//                AppConfigUtil.setFirstLaunch(false);
//                transHandler.postDelayed(() -> {
//                    if (weakReference.get() != null) {
//                        startActivity(new Intent(this, SplashActivity.class));
//                        finish();
//                    }
//                }, 600);
//            } else {
//                transHandler.postDelayed(() -> {
//                    if (weakReference.get() != null) {
//                        startActivity(new Intent(this, MainActivity.class));
//                        finish();
//                    }
//                }, 600);
//            }
//        } else {
//            // 非首次启动app/非更新后首次启动app 情况下会显示广告
//            adIv.setVisibility(View.VISIBLE);
//            skinBtn.setVisibility(View.VISIBLE);
//            showAd(flashAdvEntities);
//        }


//    ConfigFlashAdvEntity configFlashAdvEntity = null;
//
//    private void showAd(ArrayList<ConfigFlashAdvEntity> flashAdvEntities) {
//        if (flashAdvEntities.size() == 1) {
//            configFlashAdvEntity = flashAdvEntities.get(0);
//        } else {
//            int weightTotal = 0;
//            for (ConfigFlashAdvEntity entity : flashAdvEntities) {
//                weightTotal += entity.show_weight;
//            }
//            Random random = new Random();
//            int randomPoint = random.nextInt(weightTotal);
//            int lower, upper = 0;
//            for (ConfigFlashAdvEntity entity : flashAdvEntities) {
//                int gamma = entity.show_weight;
//                lower = upper;
//                upper += gamma;
//                configFlashAdvEntity = entity;
//                if (randomPoint >= lower && randomPoint < upper) {
//                    break;
//                }
//            }
//        }
//        if (configFlashAdvEntity != null && !StringUtil.isEmpty(configFlashAdvEntity.adv_images)) {
//            COUNTDOWN_SECOND = configFlashAdvEntity.life_time;
//            ADCountDown();
//            float screenP = EWGShopApp.SCREEN_H * 1.f / EWGShopApp.SCREEN_W;
//            int position = 0;
//            float dP = 0;
//            for (int i = 0; i < configFlashAdvEntity.adv_images.size(); i++) {
//                ConfigFlashAdvEntity.FlashImgEntity imgEntity = configFlashAdvEntity.adv_images.get(i);
//                float imgP = imgEntity.heigh * 1.f / imgEntity.width;
//                if (i == 0) {
//                    dP = Math.abs(screenP - imgP);
//                    position = i;
//                } else {
//                    float imgdP = Math.abs(screenP - imgP);
//                    if (dP > imgdP) {
//                        dP = imgdP;
//                        position = i;
//                    }
//                }
//            }
//            if (position < configFlashAdvEntity.adv_images.size()) {
//                ImageLoaderUtil.load(this, configFlashAdvEntity.adv_images.get(position).img, adIv);
//                adIv.setOnClickListener(v -> {
//                    HttpClient.ad_flash_adv_stat(configFlashAdvEntity.id, "2", new HttpCallBack<ResponseEntity>() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onSuccess(ResponseEntity responseEntity) {
//
//                        }
//
//                        @Override
//                        public void onFail(String msg) {
//
//                        }
//                    });
//                    startMainActivity();
//                    ActionManager.handleAction(configFlashAdvEntity.action);
//                });
//                HttpClient.ad_flash_adv_stat(configFlashAdvEntity.id, "1", new HttpCallBack<ResponseEntity>() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseEntity responseEntity) {
//
//                    }
//
//                    @Override
//                    public void onFail(String msg) {
//
//                    }
//                });
//            }
//        }
//    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;//  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    /**
     * 广告倒计时
     */
//    private void ADCountDown() {
//        Observable.interval(COUNTDOWN_DELAY_SECOND, COUNTDOWN_INTERVAL_SECOND, TimeUnit.SECONDS) //0延迟  每隔1秒触发
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
//                .take(COUNTDOWN_SECOND + 1) //设置循环次数
//                .map(aLong -> COUNTDOWN_SECOND - aLong) //从5-0
//                .subscribe(new Observer<Long>() {
//                               @Override
//                               public void onSubscribe(Disposable d) {
//                                   disposable = d;
//                               }
//
//                               @Override
//                               public void onNext(Long aLong) {
//                                   String countDown = aLong + "  " + getResources().getString(R.string.ecg_127);
//                                   skinBtn.setText(countDown);
//                               }
//
//                               @Override
//                               public void onError(Throwable e) {
//                                   startMainActivity();
//                               }
//
//                               @Override
//                               public void onComplete() {
//                                   startMainActivity();
//                               }
//                           }
//                );
//    }

    /**
     * 启动MainActivity
     */
    private void startMainActivity() {
//        cancelCountDown();
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cancelCountDown();
    }

    /**
     * 取消广告倒计时的订阅
     */
//    private void cancelCountDown() {
//        if (null != disposable && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
}
