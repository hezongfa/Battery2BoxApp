package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
public class UserEntity extends BaseEntity {
    /**
     * "mId": 1,
     * "headImg": "http://qqqqqqqqq.png",
     * "sex": 1(‘1’= 男；‘2’= 女；空是未设置),
     * "phone": "12154545",
     * "userName": "tan",
     * "email": "362839344@qq.com",
     * "balance": "100",
     * "couponsNum": 3,
     * "type": 1,
     * "token": difuduf989df8d98dsdsds，
     * "orderNum": 0，
     * "isFacebook": 0，
     * "isWeixin": 0，
     */
    @SerializedName("mid")
    public int mId;
    public String headImg;
    public int sex;
    public String phone;
    public String userName;
    public String email;
    public String balance;
    public String couponsNum;
    public int type;
    public String token;
    public String orderNum;
    public int isFacebook;
    public int isWeixin;
    public int remainingTime;
}
