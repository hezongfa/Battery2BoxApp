package com.batterbox.power.phone.app.entity;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.entity.BaseEntity;
import com.chenyi.baselib.utils.StringUtil;

/**
 * Created by ass on 2019-07-30.
 * Description
 */
public class LBShopEntity extends BaseEntity {
    /**
     * "shopId": 1,
     * "shopName": 'asdasdasd',
     * "shopIco": 'asdasdasd',
     * "businessTime": '9:00 - 18:00',
     * "shopAdress":"地址"
     * "stId": 'asdasdasd',
     * "stName": 'asdasdasd',
     * "stLogo": 'asdasdasd',
     * "boxCode":'asdasd',
     * "la": 23.448274,
     * "lo": 113.848481,
     * "rentCount": 9,
     * "returnCount": 1,
     */
    public int state;
    public String shopId;
    public String shopName;
    public String shopIco;
    public String businessTime;
    public String shopAdress;
    public int stId;
    public String stName;
    public String stLogo;
    public String boxCode;
    public double la;
    public double lo;
    public String rentCount;
    public String returnCount;
    // 本地用
    // 距离
    public double dis;

    public int getIconRes() {
//        1=Tienda商店；2=RESTAURANTE饭店；3=PUB-夜店；4=HOTEL酒店；5=GYM健身房；6=CAFETERIA咖啡店
        if (state == 1) {
            switch (stId) {
                case 1:
                    return R.mipmap.ic_marker_shop;
                case 2:
                    return R.mipmap.ic_marker_restaurant;
                case 3:
                    return R.mipmap.ic_marker_club;
                case 4:
                    return R.mipmap.ic_marker_hotel;
                case 5:
                    return R.mipmap.ic_marker_gym;
                case 6:
                    return R.mipmap.ic_marker_cafeteria;
                case 7:
                    return R.mipmap.ic_marker_supermarket;
                case 8:
                    return R.mipmap.ic_marker_golf;
                case 9:
                    return R.mipmap.ic_marker_dentisita;
                case 10:
                    return R.mipmap.ic_marker_hospital;
                case 11:
                    return R.mipmap.ic_marker_film;
                case 12:
                    return R.mipmap.ic_marker_peluqueria;
                case 13:
                    return R.mipmap.ic_marker_bears;
                default:
                    return R.mipmap.ic_marker_def;
            }
        } else {
            switch (stId) {
                case 1:
                    return R.mipmap.ic_marker_shop_n;
                case 2:
                    return R.mipmap.ic_marker_restaurant_n;
                case 3:
                    return R.mipmap.ic_marker_club_n;
                case 4:
                    return R.mipmap.ic_marker_hotel_n;
                case 5:
                    return R.mipmap.ic_marker_gym_n;
                case 6:
                    return R.mipmap.ic_marker_cafeteria_n;
                case 7:
                    return R.mipmap.ic_marker_supermarket_n;
                case 8:
                    return R.mipmap.ic_marker_golf_n;
                case 9:
                    return R.mipmap.ic_marker_dentisita_n;
                case 10:
                    return R.mipmap.ic_marker_hospital_n;
                case 11:
                    return R.mipmap.ic_marker_film_n;
                case 12:
                    return R.mipmap.ic_marker_peluqueria_n;
                case 13:
                    return R.mipmap.ic_marker_bears_n;
                default:
                    return R.mipmap.ic_marker_def;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LBShopEntity) {
            if (StringUtil.isEquals(((LBShopEntity) obj).shopId, shopId)) {
                return true;
            }
//            if (((LBShopEntity) obj).la == la && ((LBShopEntity) obj).lo == lo) {
//                return true;
//            }
        }
        return super.equals(obj);
    }
}
