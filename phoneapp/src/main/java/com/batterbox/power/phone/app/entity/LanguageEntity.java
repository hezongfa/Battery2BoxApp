package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-08-09.
 * Description
 */
public class LanguageEntity extends BaseEntity {
    public String text;
    public int type;
    public String key;

    public LanguageEntity(String text, int type, String key) {
        this.text = text;
        this.type = type;
        this.key = key;
    }
}
