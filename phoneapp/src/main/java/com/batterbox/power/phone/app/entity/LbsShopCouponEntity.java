package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

import java.util.ArrayList;

/**
 * Created by ass on 2020-02-01.
 * Description
 */
public class LbsShopCouponEntity extends BaseEntity {
    public String shopId;
    public String shopName;
    public String shopIco;
    public ArrayList<CouponEntity> shopCoupons;
}
