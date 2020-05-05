package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2020-05-04.
 * Description
 */
public class SearchUserEntity extends BaseEntity {
    /**
     *  "gfId": 3,//查找出来的用户id
     *     "username": "user-18924690153",//用户名称
     *     "headImg": "https://app.battery2box.com:8888/d4e1eb92-0f5c-446d-a54c-4f43b3289148.jpg",//用户头像
     *     "sex": null,//性别：1= 男；2= 女；空是未设置（保密），都有对应的图标
     *     "isGf":0,//是否是好友：0=不是；1=是
     *     "adress": "Confidencial"//地址
     */
    public String gfId;
    public String username;
    public String headImg;
    public int sex;
    public int isGf;
    public String adress;
    public String phone;
}
