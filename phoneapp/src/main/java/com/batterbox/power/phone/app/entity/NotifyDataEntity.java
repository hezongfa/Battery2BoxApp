package com.batterbox.power.phone.app.entity;

import android.text.SpannableString;

import com.chenyi.baselib.entity.BaseEntity;
import com.chenyi.baselib.utils.StringUtil;

import java.util.HashMap;

public class NotifyDataEntity extends BaseEntity {
    /**
     * "id": 1,
     * "registtime": "2020-04-24 17:28:35",//消息时间
     * "title": "租借信息",//标题
     * "defaultBody": "恭喜你，租借成功！",//消息开始语
     * "remarke": "感谢你的使用！",//消息结束语
     * "jumpType": 1,//跳转类型：0 or null=不进行任何操作；1=本地跳转；2=外链跳转
     * "jumpUrl": "ORDERLIST",//跳转标识：ORDERLIST（跳转到订单页面的标识）；WALLET（跳转到我的钱包标识）；SHOP?shopId=（跳转到店铺详情页标识，后面shopId是店铺id）
     * "img": null,//图片
     * "keyValueBody": {//消息结构体
     * "租借时间": "10/5/2020 15:00:00",
     * "租借地点": "Guangzhou"
     * }
     */

    public long id;
    public String registtime;
    public String title;
    public String defaultBody;
    public String remarke;
    public int jumpType;
    public String jumpUrl;
    public String img;
    public HashMap<String, String> keyValueBody;

    private String keyValueStr;

    public String getKeyValueStr() {
        if (keyValueStr == null && !StringUtil.isEmpty(keyValueBody)) {
            StringBuilder sb = new StringBuilder();
            for (String key : keyValueBody.keySet()) {
                sb.append(key).append(":").append(keyValueBody.get(key)).append("\n");
            }

            keyValueStr = sb.toString();
            keyValueStr=StringUtil.trim(keyValueStr);
        }
        return keyValueStr;
    }
}
