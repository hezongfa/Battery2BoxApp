package com.chenyi.baselib.entity;

/**
 * 网络响应类
 */

public class ResponseEntity<T> extends BaseEntity {
    //    private T message;
//    private T list;
//    private T info;
    private T data;
    private int code;
    private String msg;
    //    private boolean toLogin;
    public long force_version;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isToLogin() {
        return code == 999;
    }

    public boolean isNeedUID() {
        return code == 10000;
    }

//    public void setToLogin(boolean toLogin) {
//        this.toLogin = toLogin;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public T getInfo() {
//        return info;
//    }
//
//    public void setInfo(T info) {
//        this.info = info;
//    }
//
//    public T getList() {
//        return list;
//    }
//
//    public void setList(T list) {
//        this.list = list;
//    }
//
//    public T getMessage() {
//        return message;
//    }
//
//    public void setMessage(T message) {
//        this.message = message;
//    }
}
