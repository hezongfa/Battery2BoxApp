package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-08-09.
 * Description
 */
public class AreaCodeEntity extends BaseEntity {
    public String country;
    public String mobilePrefix;

    public AreaCodeEntity(String country, String mobilePrefix) {
        this.country = country;
        this.mobilePrefix = mobilePrefix;
    }
}
