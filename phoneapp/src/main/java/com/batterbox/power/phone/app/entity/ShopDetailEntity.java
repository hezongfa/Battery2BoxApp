package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-06.
 * Description
 */
public class ShopDetailEntity extends BaseEntity {
    /**
     * "shopId": 1,//店铺id
     * "shopName": 'asdasdasd',//店铺名字
     * "shopIco": 'asdasdasd',//店铺头像
     * "businessTime": '9:00 - 18:00',//运营时间
     * "shopAdress":"地址"//店铺地址
     * "img": [//店铺图
     * "http://qqqqqqqqq1.png",
     * "http://qqqqqqqqq2.png",
     * ]
     * "stId": 'asdasdasd',//类型id
     * "stName": 'asdasdasd',//类型名字
     * "stLogo": 'asdasdasd',//类型logo
     * "wifi": 1,//是否有wifi
     * "smoke": 1,//是否允许抽烟
     * "url": "www.baidu.com",//店铺地址
     * "phone": "15989171777"//店铺电话
     */

    public String shopId;
    public String shopName;
    public String shopIco;
    public String businessTime;
    public String shopAdress;
    public int stId;
    public String stName;
    public String stLogo;
    public ArrayList<String> img;
    public int wifi;
    public int smoke;
    public String url;
    public String email;
    public String phone;
    public int couponCon;
    public String rentCount;
    public String returnCount;
    public double la;
    public double lo;
}
