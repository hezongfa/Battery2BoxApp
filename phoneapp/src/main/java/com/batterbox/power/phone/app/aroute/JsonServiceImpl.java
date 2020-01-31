package com.batterbox.power.phone.app.aroute;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.chenyi.baselib.utils.JsonExplain;

import java.lang.reflect.Type;

/**
 * Used for json converter， ARouter 解释自定义对象
 */
@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {
    @Override
    public void init(Context context) {

    }

    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        if (input == null || clazz == null) return null;
        return JsonExplain.gson.fromJson(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        if (instance == null) return null;
        return JsonExplain.gson.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        if (input == null || clazz == null) return null;
        return JsonExplain.gson.fromJson(input, clazz);
    }
}
