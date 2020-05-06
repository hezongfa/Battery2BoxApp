package com.batterbox.power.phone.app.utils;

import android.content.Context;
import android.content.Intent;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.act.main.chat.FriendProfileActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.SearchUserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.print.FQT;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * Created by ass on 2020-05-04.
 * Description
 */
public class ScanUtil {

    public static void scan(BaseActivity activity) {
        ScanBarCodeHelper.scan(activity, result -> {
            FQL.e("onScanSuccess: " + result);
            String scanUrl = result.getContent();
            if (!StringUtil.isEmpty(scanUrl)) {
                if (scanUrl.contains("?box=")) {
                    getDeviceInfo(activity, scanUrl);
                } else if (scanUrl.contains("?battery=")) {
                    //TODO
                } else if (scanUrl.contains("?coupon=")) {
                    String[] ss = scanUrl.split("\\?coupon=");
                    if (ss.length > 1) {
                        ARouteHelper.coupon_get(scanUrl.split("\\?coupon=")[1]).navigation();
                    }
                } else if (scanUrl.contains("?uu=")) {
                    searchUser(activity, scanUrl);
                } else if (scanUrl.contains("?shop=")) {
                    String[] ss = scanUrl.split("\\?shop=");
                    if (ss.length > 1) {
                        LBShopEntity lbShopEntity = new LBShopEntity();
                        lbShopEntity.shopId = scanUrl.split("\\?shop=")[1];
                        ARouteHelper.shop_detail(lbShopEntity).navigation();
                    }
                } else if (scanUrl.contains("?shopUrl=")) {
                    String[] ss = scanUrl.split("\\?shopUrl=");
                    if (ss.length > 1) {
                        String webUrl = scanUrl.split("\\?shopUrl=")[1] + "&device=Android&token=" + StringUtil.fixNullStr(UserUtil.getToken());
                        ARouteHelper.hybrid_nav(webUrl).navigation();
                    }
                }

            }
        });
    }

    private static void searchUser(Context context, String url) {
        HttpClient.getInstance().im_searchMember(url, new NormalHttpCallBack<ResponseEntity<SearchUserEntity>>(context) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<SearchUserEntity> responseEntity) {
                if (responseEntity.getData() != null) {
                    if (responseEntity.getData().isGf == 1) {
                        ContactItemBean contact = new ContactItemBean();
                        contact.setAvatarurl(responseEntity.getData().headImg);
                        contact.setFriend(true);
                        contact.setGroup(false);
                        contact.setId(responseEntity.getData().gfId);
                        contact.setNickname(responseEntity.getData().username);
                        Intent intent = new Intent(BatterBoxApp.getInstance(), FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                        BatterBoxApp.getInstance().startActivity(intent);
                    } else {
                        ARouteHelper.chat_add_more(responseEntity.getData(), null).navigation();
                    }
                }
            }

            @Override
            public void onFail(ResponseEntity<SearchUserEntity> responseEntity, String msg) {
                FQT.showShort(context, msg);
            }
        });
    }

    private static void getDeviceInfo(Context context, String url) {
        HttpClient.getInstance().find_box(url, new NormalHttpCallBack<ResponseEntity<DeviceEntity>>(context) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<DeviceEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    ARouteHelper.device_detail(responseEntity.getData()).navigation();
                }
            }

            @Override
            public void onFail(ResponseEntity<DeviceEntity> responseEntity, String msg) {
                FQT.showShort(context, msg);
            }
        });
    }
}
