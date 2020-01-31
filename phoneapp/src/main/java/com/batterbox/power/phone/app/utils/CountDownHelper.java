package com.batterbox.power.phone.app.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ass on 2018/10/24.
 */

public class CountDownHelper {

    public static Disposable buildCountDown(long time, Observer<Long> callBack,int period, TimeUnit timeUnit) {
        final Disposable[] disposable = new Disposable[1];
        Observable.interval(0, period, timeUnit) //0延迟  每隔1秒触发
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .take(time + 1) //设置循环次数
                .map(aLong -> time - aLong) //从5-0
                .subscribe(new Observer<Long>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   disposable[0] = d;
                               }

                               @Override
                               public void onNext(Long aLong) {
                                   callBack.onNext(aLong);
                               }

                               @Override
                               public void onError(Throwable e) {
                               }

                               @Override
                               public void onComplete() {
                               }
                           }
                );
        return disposable[0];
    }

    public static Disposable buildCountDown(long time, Observer<Long> callBack) {
        return buildCountDown(time, callBack, 1,TimeUnit.SECONDS);
    }

    public static void cancelCountDown(Disposable disposable) {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
