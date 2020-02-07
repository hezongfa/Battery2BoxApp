package com.batterbox.power.phone.app.aroute;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.batterbox.power.phone.app.entity.BorrowResultEntity;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.OrderEntity;
import com.batterbox.power.phone.app.entity.OrderStateEntity;

import java.util.ArrayList;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
public class ARouteHelper {
    public static final String AREA_CODE = "/app/area/code";
    public static final String LOGIN = "/app/user/login";
    public static final String REGISTER = "/app/user/register";
    public static final String VERCODE = "/app/user/VerCode";
    public static final String FORGETPWD = "/app/user/forgetPwd";
    public static final String APP_SCAN = "/app/scan";
    public static final String DEVICE_DETAIL = "/app/device/detail";
    public static final String BORROW_RESULT = "/app/borrow/result";
    public static final String BORROW_SUCCESS = "/app/borrow/success";
    public static final String ORDER_LIST = "/app/order/list";
    public static final String ORDER_DETAIL = "/app/order/detail";
    public static final String WALLET_MY = "/app/wallet/my";
    public static final String WALLET_CARD_EDIT = "/app/wallet/card_edit";
    public static final String MYCARD = "/app/wallet/mycard";
    public static final String SHOP_DETAIL = "/app/shop/detail";
    public static final String SHOP_LIST = "/app/shop/list";
    public static final String HELPER_DETAIL = "/app/helper/detail";
    public static final String SETTING = "/app/setting";
    public static final String SETTING_LANGUAGE = "/app/setting/language";
    public static final String SETTING_CHANGEPWD = "/app/setting/change_pwd";
    public static final String USER_INFO = "/app/user/info";
    public static final String USER_INFO_EDIT = "/app/user/info_edit";
    public static final String COOPERATION = "/app/cooperation";
    public static final String USER_BILL = "/app/user/bill";
    public static final String HYBRID_NAV = "/hybrid/nav";

    public static final String RECHARGE_LIST = "/app/user/recharge_list";
    public static final String COUPON = "/app/coupon";
    public static final String COUPON_GET = "/app/coupon_get";
    public static final String PROMOTION = "/app/promotion";
    public static final String TIME_RECORD = "/app/time_record";
    public static final String SHARE = "/app/SHARE";

    public static final String PHOTO_LIST = "/app/photo_list";
    public static final String SHOW_BIG_IMGS = "/app/show_big_imgs";
    public static Postcard show_big_imgs(ArrayList<String> img, String url) {
        return ARouter.getInstance().build(SHOW_BIG_IMGS).withObject("img", img).withString("url", url);
    }


    public static Postcard area_code() {
        return ARouter.getInstance().build(AREA_CODE);
    }

    public static Postcard login() {
        return ARouter.getInstance().build(LOGIN);
    }

    public static Postcard register() {
        return ARouter.getInstance().build(REGISTER);
    }

    public static Postcard vercode(String phone) {
        return ARouter.getInstance().build(VERCODE).withString("phone", phone);
    }

    public static Postcard forgetpwd() {
        return ARouter.getInstance().build(FORGETPWD);
    }


    public static Postcard scan() {
        return ARouter.getInstance().build(APP_SCAN);
    }

    public static Postcard device_detail(DeviceEntity deviceEntity) {
        return ARouter.getInstance().build(DEVICE_DETAIL).withObject("deviceEntity", deviceEntity);
    }

    public static Postcard borrow_result(BorrowResultEntity borrowResultEntity) {
        return ARouter.getInstance().build(BORROW_RESULT).withObject("borrowResultEntity", borrowResultEntity);
    }

    public static Postcard borrow_success(OrderStateEntity orderStateEntity) {
        return ARouter.getInstance().build(BORROW_SUCCESS).withObject("orderStateEntity", orderStateEntity);
    }

    public static Postcard order_list() {
        return ARouter.getInstance().build(ORDER_LIST);
    }

    public static Postcard order_detail(OrderEntity orderEntity) {
        return ARouter.getInstance().build(ORDER_DETAIL).withObject("orderEntity", orderEntity);
    }

    public static Postcard wallet_my() {
        return ARouter.getInstance().build(WALLET_MY);
    }

    public static Postcard wallet_card_edit() {
        return ARouter.getInstance().build(WALLET_CARD_EDIT);
    }

    public static Postcard mycard() {
        return ARouter.getInstance().build(MYCARD);
    }

    public static Postcard shop_detail(LBShopEntity lbShopEntity) {
        return ARouter.getInstance().build(SHOP_DETAIL).withObject("lbShopEntity", lbShopEntity);
    }

    public static Postcard shop_list(ArrayList<LBShopEntity> lbShopEntities) {
        return ARouter.getInstance().build(SHOP_LIST).withObject("lbShopEntities", lbShopEntities);
    }

    public static Postcard helper_detail(String lat, String lng) {
        return ARouter.getInstance().build(HELPER_DETAIL).withString("lat", lat).withString("lng", lng);
    }

    public static Postcard setting() {
        return ARouter.getInstance().build(SETTING);
    }

    public static Postcard setting_language() {
        return ARouter.getInstance().build(SETTING_LANGUAGE);
    }

    public static Postcard setting_changepwd() {
        return ARouter.getInstance().build(SETTING_CHANGEPWD);
    }


    public static Postcard user_info() {
        return ARouter.getInstance().build(USER_INFO);
    }

    public static Postcard user_info_edit(String title, String content, String hint) {
        return ARouter.getInstance().build(USER_INFO_EDIT).withString("title", title).withString("content", content).withString("hint", hint);
    }

    public static Postcard cooperation() {
        return ARouter.getInstance().build(COOPERATION);
    }

    public static Postcard user_bill() {
        return ARouter.getInstance().build(USER_BILL);
    }

    public static Postcard hybrid_nav(String url) {
        return ARouter.getInstance().build(HYBRID_NAV).withString("url", url);
    }

    public static Postcard hybrid_nav(String url, String defTitle) {
        return ARouter.getInstance().build(HYBRID_NAV).withString("url", url).withString("defTitle", defTitle);
    }

    public static Postcard recharge_list() {
        return ARouter.getInstance().build(RECHARGE_LIST);
    }

    public static Postcard coupon() {
        return ARouter.getInstance().build(COUPON);
    }
    public static Postcard coupon_get() {
        return ARouter.getInstance().build(COUPON_GET);
    }


    public static Postcard promotion() {
        return ARouter.getInstance().build(PROMOTION);
    }

    public static Postcard time_record() {
        return ARouter.getInstance().build(TIME_RECORD);
    }

    public static Postcard share() {
        return ARouter.getInstance().build(SHARE);
    }

    public static Postcard photo_list(ArrayList<String> img) {
        return ARouter.getInstance().build(PHOTO_LIST).withObject("img", img);
    }

}
