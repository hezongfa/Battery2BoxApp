package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-08-23.
 * Description
 */
public class BillEntity extends BaseEntity {
    /**
     * "mwdId": 796,//记录id
     * "mwdCode": "19156468259349064277",//系统编号
     * "mwdType": 2,//‘1’= 入账；‘2’= 出账；
     * "transactionType": 22,//交易类型编号
     * "transactionName": "余额支付订单",//交易类型名称
     * "transactionTime": "2019-08-02 02:04:14",//交易时间
     * "payCode": "YE-19156468259349064277",//交易编号
     * "changeBalance": 0//交易金额
     */
    public String mwdId;
    public String mwdCode;
    public String mwdType;
    public String transactionType;
    public String transactionName;
    public String transactionTime;
    public String payCode;
    public String changeBalance;
}
