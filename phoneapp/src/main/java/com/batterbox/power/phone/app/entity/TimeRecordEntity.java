package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-09-29.
 * Description
 */
public class TimeRecordEntity extends BaseEntity {
    /**
     * "mfdId": 1,//记录id
     * "mfdCode": "123",//订单编号
     * "mfdType": 1,//记录方式：‘1’= 入账；‘2’= 出账
     * "transactionType": "充值赠送",//记录类型，可直接用来显示
     * "transactionTime": "2019-09-02 01:33:23",//记录时间
     * "mfdChange": 1,//变化时长，单位：分钟
     * "mfdNow": 1,//剩余时长，单位：分钟
     * "bshopName": null//租借店铺名，只有是订单支付的记录才有该记录值
     */
    public String mfdId;
    public String mfdCode;
    public int mfdType;
    public String transactionType;
    public String transactionTime;
    public String mfdChange;
    public String mfdNow;
    public String bshopName;
}
