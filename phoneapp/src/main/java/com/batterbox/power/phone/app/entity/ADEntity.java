package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2020-01-29.
 * Description
 */
public class ADEntity extends BaseEntity {
    public long id;
    //类型：Web(跳转链接)；AppPage(app页面)
    public String type;
    //类型对应的值：type=Web(跳转链接，这种情况下，只有一个值)；type=AppPage(app页面，这种情况下，有多个值，有跳转充值页Recharge，邀请推广页popu，店铺详情页shop)
    public String[] typeValue;
    public String img;
}
