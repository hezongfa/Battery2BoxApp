package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2020-01-31.
 * Description
 */
public class CouponEntity extends BaseEntity {
    /**
     * "id": 1,//唯一标识
     * "name": "test",//优惠卷名称
     * "type": 2,//优惠卷类型：1=充电时长优惠卷；2=商家减免优惠卷；3=商家折扣优惠卷
     * "img": "http://....",//优惠图片
     * "typeName": "减免优惠卷",//类型名称（支持多语言）
     * "typeValue": 5,//优惠值
     * "limitValue": 50,//限制条件：比如减免优惠卷（满足50减5）
     * "isMerge": 0,//是否允许同时使用：0=不允许；1=允许
     * "registtime": "2019-12-31 18:59:50",//开始时间
     * "effectiveTime": "2020-01-30 18:59:53",//结束时间
     * "remarke": "消费50.0欧元，减免5.0欧元"//说明
     */
    public long id;
    public String name;
    public int type;
    public String img;
    public String typeName;
    public String typeValue;
    public String limitValue;
    public int isMerge;
    public String registtime;
    public String effectiveTime;
    public String remarke;

    //是否可以使用：1=可以使用；2=不可以使用
    public int isUse;
    public String useTime;

    public boolean isExpain;


//      "id": 1,//记录id
//              "name": "10分钟充电卷",//优惠卷名称
//              "type": 1,
//              "typeName": "充电卷",//类型名称
//              "typeValue": 10,//值
//              "registtime": "2020-02-03 16:04:58",//领取时间
//              "effectiveTime": "05/02/2020",//过期时间
//              "useTime": null,//使用时间，null就是没使用
//              "remarke": "这是免费时长的充电卷",//说明
//              "img": "http://dev.battery2box.com:8890/330e645c-09bb-4dc6-b701-4c2e9d777989.jpg",//图片
//              "isUse": 1//是否可以使用：1=可以使用；2=不可以使用
}
