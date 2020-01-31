package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-08-08.
 * Description
 */
public class OrderStateEntity extends BaseEntity {
    /**
     * "state":1,//订单状态
     * "price":2.0,//每小时计费价格
     * "freeMinute":20,//免费时长（分钟单位）
     * "highCost":5,//每天最高收费
     * "deposit":20//押金金额
     */
    public int state;
    public String price;
    public String freeMinute;
    public String highCost;
    public String deposit;
}
