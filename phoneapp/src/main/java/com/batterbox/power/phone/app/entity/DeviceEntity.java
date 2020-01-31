package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-07-30.
 * Description
 */
public class DeviceEntity extends BaseEntity {
    /**
     * "boxCode": "2910555",
     * "state": 1,
     * "type": 1,
     * "rentCost": 2,
     * "highCost": 10,
     * "freeUsetime": 5,
     * "isblock": 2,
     * "shopName": "test shop",
     * "businessTime": "every day 9:00 ~ 24:00",
     * "shopIco": "http://aaa.png",
     */
    public String boxCode;
    public int state;
    public int type;
    public String rentCost;
    public double highCost;
    public int freeUsetime;
    public int isblock;
    public String businessTime;
    public String shopName;
    public String shopIco;
    public double depoitCost;
    public String giveTime;
}
