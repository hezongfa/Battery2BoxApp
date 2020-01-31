package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-08-06.
 * Description
 */
public class CardEntity extends BaseEntity {
    /**
     * "mcId": 1,//卡id
     * "cardNum": "7579017934XXXXXXX",//卡号
     * "cardUserName": null,//持卡人名字
     * "cardExpirationData": "1910",//卡过期时间
     * "cvv": "***",//卡cvv号
     * "cardAlias": null,//卡别名，可以不用
     * "createTime": "2019-07-11 13:43:29",//创建时间
     * "updateTime": "2019-07-11 13:43:29",//更新时间
     * "cardType": "MC",//卡类型：MC=master,VISA=visa，CB=不知道
     * "mid": null
     */
    public String mcId;
    public String cardNum;
    public String cardUserName;
    public String cardExpirationData;
    public String cvv;
    public String cardAlias;
    public String createTime;
    public String updateTime;
    public String cardType;
    public String mid;
}
