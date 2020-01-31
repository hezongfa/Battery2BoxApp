package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

/**
 * Created by ass on 2019-10-15.
 * Description
 */
public class SharePageEntity extends BaseEntity {
    /**
     * "invitationTimes": 0,//总送时长（分钟单位）
     * "giveTime": 60,//邀请一个用户所得的时长（分钟单位）
     * "invitations": 0,//邀请人数
     * "url": "https://app.battery2box.com/invitation/2"//分享的链接
     */
    public long invitationTimes;
    public long giveTime;
    public long invitations;
    public String url;
}
