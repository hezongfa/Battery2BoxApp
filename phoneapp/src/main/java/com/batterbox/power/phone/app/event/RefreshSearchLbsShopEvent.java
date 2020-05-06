package com.batterbox.power.phone.app.event;

import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.chenyi.baselib.event.EventBase;

import java.util.ArrayList;

/**
 * Created by ass on 2020-05-06.
 * Description
 */
public class RefreshSearchLbsShopEvent extends EventBase {
    public ArrayList<LBShopEntity> lbShopEntities;

    public RefreshSearchLbsShopEvent(ArrayList<LBShopEntity> lbShopEntities) {
        this.lbShopEntities = lbShopEntities;
    }
}
