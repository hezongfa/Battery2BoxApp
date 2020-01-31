package com.batterbox.power.phone.app.entity;

import android.content.Context;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.entity.BaseEntity;
import com.chenyi.baselib.utils.StringUtil;

/**
 * Created by ass on 2019-08-03.
 * Description
 */
public class OrderEntity extends BaseEntity {
    public OrderEntity() {
    }

    public OrderEntity(boolean showSection, boolean isDone) {
        this.isDone = isDone;
        this.showSection = showSection;
    }

    /**
     * "orderId": 1,//订单id
     * "code": "2910-9193-9193-1232",//订单编号
     * "state": 1,//订单状态
     * "rentTime": "2019-03-23 23:35:35",//租借时间
     * "rentShopName": "店铺名",//租借店铺名
     * "returnTime": "2019-03-23 23:35:35",//归还时间
     * "returnShopName": "店铺名",//归还店铺名称
     * "price": 1,//每个小时价格
     * "useTime": 10,//使用时长
     * "freeTime": 0,//免费时长
     * "highCost":20//每天封顶消费
     * "cost": 10,//订单价格
     * "payState":2//支付状态
     * "payType": 1,//支付方式
     * "depoitCost":20,//冻结金额
     * "depoitType":1,//冻结方式
     * "depoitState":2//冻结状态
     */

    public long orderId;
    public String code;
    //订单状态：‘0’= 执行中；‘1’= 租借中；‘2’= 已归还；‘3’= 已撤销；‘4’= 超时结算；‘5’= 用户购买；
    public int state;
    public String rentTime;
    public String rentShopName;
    public String returnTime;
    public String returnShopName;
    public String price;
    public long useTime;
    public long freeTime;
    public String highCost;
    public String cost;
    //支付状态:‘1’= 未支付；‘2’= 已支付；
    public int payState;
    //支付方式:‘1’= 信用卡支付；‘2’= 余额支付；‘3’=免租金；
    public int payType;
    public String depoitCost;
    //冻结方式:‘1’= 信用卡；‘2’= 余额；‘3’= 免押金；
    public int depoitType;
    //冻结状态‘1’= 冻结；‘2’= 已解冻；
    public int depoitState;
    //当前使用了的时间（秒）
    public float sysTime;
    //抵扣时长
    public String deductionTime;

    public boolean showSection;
    public boolean isDone;

    public String getState(Context context) {
        String stateStr = "";
        switch (state) {
            case 0:
                stateStr = context.getString(R.string.order_6);
                break;
            case 1:
                stateStr = context.getString(R.string.order_7);
                break;
            case 2:
                stateStr = context.getString(R.string.order_8);
                break;
            case 3:
                stateStr = context.getString(R.string.order_9);
                break;
            case 4:
                stateStr = context.getString(R.string.order_10);
                break;
            case 5:
                stateStr = context.getString(R.string.order_11);
                break;
            default:
                break;
        }
        return stateStr;
    }

    public String getPayState(Context context) {
        String stateStr = "";
        switch (payState) {
            case 1:
                stateStr = context.getString(R.string.order_12);
                break;
            case 2:
                stateStr = context.getString(R.string.order_13);
                break;
        }
        return stateStr;
    }

    public String getPayType(Context context) {
        String str = "";
        switch (payType) {
            case 1:
                str = context.getString(R.string.order_34);
                break;
            case 2:
                str = context.getString(R.string.order_35);
                break;
            case 3:
                str = context.getString(R.string.order_36);
                break;
        }
        return str;
    }

//    public String getDepoitState(Context context) {
//        String str = "";
//        switch (depoitState) {
//            case 1:
//                str = context.getString(R.string.order_37);
//                break;
//            case 2:
//                str = context.getString(R.string.order_38);
//                break;
//        }
//        return str;
//    }

//    public String getDepoitType(Context context) {
//        String str = "";
//        switch (depoitType) {
//            case 1:
//                str = context.getString(R.string.order_39);
//                break;
//            case 2:
//                str = context.getString(R.string.order_40);
//                break;
//            case 3:
//                str = context.getString(R.string.order_41);
//                break;
//        }
//        return str;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderEntity && !showSection && !((OrderEntity) obj).showSection) {
            return StringUtil.isEquals(orderId, ((OrderEntity) obj).orderId);
        }
        return super.equals(obj);
    }
}
