package com.chenyi.baselib.utils.http;

/**
 * Created by ass on 2018/3/21.
 */

public interface IHttpView {

    void onIHttpViewShow(Object target);

    void onIHttpViewClose();

    boolean isViewCanceled();
}
