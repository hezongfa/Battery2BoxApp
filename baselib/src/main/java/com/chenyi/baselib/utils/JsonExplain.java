package com.chenyi.baselib.utils;

import com.chenyi.baselib.utils.mgson.MGson;
import com.chenyi.baselib.utils.print.FQL;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class JsonExplain {

    public static class StringConverter implements JsonSerializer<String>,
            JsonDeserializer<String> {
        @Override
        public JsonElement serialize(String src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            if (src == null) {
                return new JsonPrimitive("");
            } else {
                return new JsonPrimitive(src.toString());
            }
        }

        @Override
        public String deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
                throws JsonParseException {
            return json.getAsJsonPrimitive().getAsString();
        }
    }

    public static <T> List<T> explainArrayJson(String josnData, Class<T[]> clazz) {
        try {
            T[] arr = gson.fromJson(josnData, clazz);
            return Arrays.asList(arr);
//            T[] arr = JSON.parseObject(josnData, clazz);
//            return arr;
        } catch (Exception e) {
            return null;
        }
    }

//    public static <T> List<T> explainListJson(String josnData) {
//        try {
//
//            List<T> arr = gson.fromJson(josnData,new TypeToken<List<T>>() {}.getType());
////            BaseEntity[] arr = JSON.parseObject(josnData, clazz);
//            return arr;
//        } catch (Exception e) {
//            FQL.e(e.toString());
//            return new ArrayList<T>();
//        }
//        // or return Arrays.asList(new
//        // Gson().fromJson(s, clazz)); for a
//        // one-liner
//    }

    public static <T> T explainJson(String jsonData, Class<T> c) {
//        return JSON.parseObject(jsonData, c);
        try {
            T t = gson.fromJson(jsonData, c);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T explainJson(String jsonData, Type type) {
//        return JSON.parseObject(jsonData, c);
        try {
            T t = gson.fromJson(jsonData,type);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Gson gson = MGson.newGson();

//    private static Gson getGson() {
//        GsonBuilder gb = new GsonBuilder();
//        gb.registerTypeAdapter(String.class, new StringConverter());
//        Gson gson = gb.create();
//        return gson;
//    }


    /**
     * JSon数据解析
     *
     * @param
     * @param key
     * @return
     */
    public static String getStringValue(String response, String key) {

        try {
            if (StringUtil.isEmpty(response)) {
                return "";
            }
            JSONObject ob = new JSONObject(response);
            String value = ob.getString(key);
            if ("null".equals(value)) {
                value = "";
            }
            return value;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            FQL.e(e.toString());
            return "";
        }
    }

    /**
     * JSon数据解析
     *
     * @param
     * @param key
     * @return
     */
    public static Integer getIntValue(String response, String key) {

        try {
            JSONObject ob = new JSONObject(response);
            int value = ob.getInt(key);
            return value;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            FQL.e(e.toString());
            return -1;
        }
    }

    /**
     * JSon数据解析
     *
     * @param
     * @param key
     * @return
     */
    public static Long getLongValue(String response, String key) {

        try {
            JSONObject ob = new JSONObject(response);
            return ob.getLong(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            FQL.e(e.toString());
            return -1L;
        }
    }

    /**
     * JSon数据解析
     *
     * @param
     * @param key
     * @return
     */
    public static Double getDoubleValue(String response, String key) {

        try {
            JSONObject ob = new JSONObject(response);
            double value = ob.getDouble(key);
            return value;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            FQL.e(e.toString());
            return -1d;
        }
    }

    /**
     * JSon数据解析
     *
     * @param
     * @param key
     * @return
     */
    public static boolean getBooleanValue(String response, String key) {

        try {
            if (StringUtil.isEmpty(response)) {
                return false;
            }
            JSONObject ob = new JSONObject(response);
            boolean value = ob.getBoolean(key);
            return value;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            FQL.e(e.toString());
            return false;
        }
    }

    /**
     * 把对象转json
     *
     * @param object
     * @return
     */
    public static String buildJson(Object object) {
        if (object == null) return "";
        return gson.toJson(object);
    }

}
