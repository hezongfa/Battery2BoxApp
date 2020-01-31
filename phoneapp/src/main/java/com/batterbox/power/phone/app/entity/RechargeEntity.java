package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-09-04.
 * Description
 */
public class RechargeEntity extends BaseEntity {
    /**
     * "rcId": 1,//金额id，发起充值的时候用到
     *       "money": 5,//金额
     *       "discountMoney": 0,//优惠金额
     *       "giveMoney": 0,//赠送金额
     *       "giveTime": 50,//赠送充电时长
     *       "createTime": "10/08/2019 11:42:45",
     *       "updateTime": null
     */
    public String rcId;
    public String money;
    public String discountMoney;
    public String giveMoney;
    public String giveTime;
    public String createTime;
    public String updateTime;
}
