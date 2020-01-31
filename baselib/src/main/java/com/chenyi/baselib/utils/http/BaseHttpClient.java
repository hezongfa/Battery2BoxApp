package com.chenyi.baselib.utils.http;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by mac on 04/01/2018.
 */

public abstract class BaseHttpClient<T> {
    private T api;
    private HttpUtilBuilder<T> httpUtilBuilder;
    private BuilderI builderI;

//    private static class HttpClientSingleton {
//        private static final HttpClient INSTANCE = new HttpClient();
//    }

    public BaseHttpClient(BuilderI builderI, Class clazz) {
        this.builderI = builderI;
        httpUtilBuilder = new HttpUtilBuilder<>(clazz, builderI.getDomainUrl(), new BasicParamsInterceptor.Builder().build());
    }

    //    public static HttpClient getInstance() {
//        return HttpClientSingleton.INSTANCE;
//    }
    public T getService() {
        return api == null ? api = httpUtilBuilder.getService() : api;
    }

    public void resetService() {
        httpUtilBuilder.resetBaseUrl(builderI.getDomainUrl());
        api = httpUtilBuilder.getService();
    }

    protected Map<String, Object> defaultPms() {
        Map<String, Object> map = new HashMap<>();
//        map.put("version", BuildConfig.VERSION_NAME);
//        map.put("device", "ANDROID");
        return map;
    }


}
