package com.batterbox.power.phone.app.http;

import com.batterbox.power.phone.app.entity.ADDataEntity;
import com.batterbox.power.phone.app.entity.AreaCodeDataEntity;
import com.batterbox.power.phone.app.entity.BillEntity;
import com.batterbox.power.phone.app.entity.BorrowResultEntity;
import com.batterbox.power.phone.app.entity.CardEntity;
import com.batterbox.power.phone.app.entity.CouponEntity;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.LbsShopCouponEntity;
import com.batterbox.power.phone.app.entity.OrderEntity;
import com.batterbox.power.phone.app.entity.OrderStateEntity;
import com.batterbox.power.phone.app.entity.RechargeEntity;
import com.batterbox.power.phone.app.entity.SharePageEntity;
import com.batterbox.power.phone.app.entity.ShopDetailEntity;
import com.batterbox.power.phone.app.entity.TimeRecordEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.entity.VersionEntity;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.http.BaseHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author ass
 * @date 2018/11/28
 * Description
 */
public class HttpClient extends BaseHttpClient<Api> {
    @Override
    protected Map<String, Object> defaultPms() {
        Map<String, Object> def = super.defaultPms();
        def.put("device", "Android");
        def.put("token", UserUtil.getToken());
        def.put("Accept-Language", StringUtil.fixNullStr(LanguageUtil.getLanguage(), LanguageUtil.ES));
        return def;
    }

    protected <T extends ResponseEntity> void query(Observable<T> observable, NormalHttpCallBack<T> callBack) {
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NormalHttpCallBack<T>(callBack != null ? callBack.iHttpView : null, callBack != null ? callBack.target : null) {
                    @Override
                    public void onStart() {
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onSuccess(T responseEntity) {
                        if (callBack != null) {
                            callBack.onSuccess(responseEntity);
                        }
                        observable.unsubscribeOn(AndroidSchedulers.mainThread());
                    }

                    @Override
                    public void onFail(T responseEntity, String msg) {
                        if (callBack != null) {
                            callBack.onFail(responseEntity, msg);
                        }
                        observable.unsubscribeOn(AndroidSchedulers.mainThread());
                    }

                    @Override
                    protected void onNextFail(T responseEntity) {
                        if (callBack != null) {
                            callBack.onNextFail(responseEntity);
                        }
                    }
                }.setIgnoreLogin(callBack != null && callBack.ignoreLogin));
    }

    private static class HttpClientSingleton {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    public static HttpClient getInstance() {
        return HttpClientSingleton.INSTANCE;
    }

    private HttpClient() {
        super(DomainHelper::getBaseUrl, Api.class);
    }

    public void uploadImg(String fileUrl, NormalHttpCallBack<ResponseEntity<String>> callBack) {

        File file = new File(fileUrl);
        // 创建 RequestBody，用于封装构建RequestBody
        // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // 添加描述
//        String descriptionString = "hello, 这是文件描述";
//        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        // 执行请求
        Observable<ResponseEntity<String>> observable = getInstance().getService().uploadSingleImg(defaultPms(), body);
        query(observable, callBack);
    }


    public void openApi_countryPrefix(NormalHttpCallBack<AreaCodeDataEntity> callBack) {
        Observable<AreaCodeDataEntity> observable = getInstance().getService().openApi_countryPrefix(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void findMemExistByPhoneAndArea(String areaCode, String username, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().findMemExistByPhoneAndArea(defaultPms(), areaCode, username);
        query(observable, callBack);
    }

    public void register(String areaCode, String username, String code, String password, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().register(defaultPms(), areaCode, username, code, password);
        query(observable, callBack);
    }


    public void login(String areaCode, String username, String password, NormalHttpCallBack<ResponseEntity<UserEntity>> callBack) {
        Observable<ResponseEntity<UserEntity>> observable = getInstance().getService().login(defaultPms(), areaCode, username, password);
        query(observable, callBack);
    }

    public void codeIsCorrect(String username, String code, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().codeIsCorrect(defaultPms(), username, code);
        query(observable, callBack);
    }


    public void user_info(NormalHttpCallBack<ResponseEntity<UserEntity>> callBack) {
        Observable<ResponseEntity<UserEntity>> observable = getInstance().getService().user_info(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void getVerCode(String areaCode, String username, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().getVerCode(defaultPms(), areaCode, username);
        query(observable, callBack);
    }


    public void bs(double lat, double lng, NormalHttpCallBack<ResponseEntity<ArrayList<LBShopEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<LBShopEntity>>> observable = getInstance().getService().bs(defaultPms(), lat, lng);
        query(observable, callBack);
    }

    public void find_box(String url, NormalHttpCallBack<ResponseEntity<DeviceEntity>> callBack) {
        Observable<ResponseEntity<DeviceEntity>> observable = getInstance().getService().find_box(defaultPms(), url);
        query(observable, callBack);
    }

    public void bs_findCouponsByShop(String shopId, NormalHttpCallBack<ResponseEntity<ArrayList<CouponEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<CouponEntity>>> observable = getInstance().getService().bs_findCouponsByShop(defaultPms(), shopId);
        query(observable, callBack);
    }

    public void bs_findShopCoupons(double lat, double lng, NormalHttpCallBack<ResponseEntity<ArrayList<LbsShopCouponEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<LbsShopCouponEntity>>> observable = getInstance().getService().bs_findShopCoupons(defaultPms(), lat, lng);
        query(observable, callBack);
    }


    public void order_borrow(String boxCode, NormalHttpCallBack<ResponseEntity<BorrowResultEntity>> callBack) {
        Observable<ResponseEntity<BorrowResultEntity>> observable = getInstance().getService().order_borrow(defaultPms(), boxCode);
        query(observable, callBack);
    }

    public void order_state(String orderCode, NormalHttpCallBack<ResponseEntity<OrderStateEntity>> callBack) {
        Observable<ResponseEntity<OrderStateEntity>> observable = getInstance().getService().order_state(defaultPms(), orderCode);
        query(observable, callBack);
    }

    public void order_UnfinishOrder(NormalHttpCallBack<ResponseEntity<OrderEntity>> callBack) {
        Observable<ResponseEntity<OrderEntity>> observable = getInstance().getService().order_UnfinishOrder(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void order_findMyOrderList(int page, int pageSize, NormalHttpCallBack<ResponseEntity<ArrayList<OrderEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<OrderEntity>>> observable = getInstance().getService().order_findMyOrderList(defaultPms(), page, pageSize);
        query(observable, callBack);
    }

    public void us_freetimeDetail(int page, int pageSize, NormalHttpCallBack<ResponseEntity<ArrayList<TimeRecordEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<TimeRecordEntity>>> observable = getInstance().getService().us_freetimeDetail(defaultPms(), page, pageSize);
        query(observable, callBack);
    }

    public void us_setUserInfo(String nickName, String logoUrl, String email, NormalHttpCallBack<ResponseEntity> callBack) {
        Map<String, Object> map = defaultPms();
        if (!StringUtil.isEmpty(nickName)) {
            map.put("nickName", nickName);
        }
        if (!StringUtil.isEmpty(logoUrl)) {
            map.put("logoUrl", logoUrl);
        }
        if (!StringUtil.isEmpty(email)) {
            map.put("email", email);
        }
        Observable<ResponseEntity> observable = getInstance().getService().us_setUserInfo(defaultPms(), map);
        query(observable, callBack);
    }

    public void us_tieCard(String cardNum, String cardUserName, String cardExpirationData, String cvv, String cardType, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().us_tieCard(defaultPms(), cardNum, StringUtil.fixNullStr(cardUserName), cardExpirationData, cvv, cardType);
        query(observable, callBack);
    }


    public void us_queryCard(NormalHttpCallBack<ResponseEntity<CardEntity>> callBack) {
        Observable<ResponseEntity<CardEntity>> observable = getInstance().getService().us_queryCard(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void us_deleCard(String mcId, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().us_deleCard(defaultPms(), mcId);
        query(observable, callBack);
    }

    public void us_modifyPwd(String oldPwd, String newPwd, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().us_modifyPwd(defaultPms(), oldPwd, newPwd);
        query(observable, callBack);
    }

    public void us_billDetail(int page, int pageSize, NormalHttpCallBack<ResponseEntity<ArrayList<BillEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<BillEntity>>> observable = getInstance().getService().us_billDetail(defaultPms(), page, pageSize);
        query(observable, callBack);
    }

    public void us_getNewVersion(NormalHttpCallBack<ResponseEntity<VersionEntity>> callBack) {
        Observable<ResponseEntity<VersionEntity>> observable = getInstance().getService().us_getNewVersion(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void us_myInvitation(NormalHttpCallBack<ResponseEntity<SharePageEntity>> callBack) {
        Observable<ResponseEntity<SharePageEntity>> observable = getInstance().getService().us_myInvitation(defaultPms(), defaultPms());
        query(observable, callBack);
    }

    public void bs_findShopDetail(String shopId, NormalHttpCallBack<ResponseEntity<ShopDetailEntity>> callBack) {
        Observable<ResponseEntity<ShopDetailEntity>> observable = getInstance().getService().bs_findShopDetail(defaultPms(), shopId);
        query(observable, callBack);
    }

    public void api_bs_deviceFault(String faultCode, String la, String lo, String boxCode, String content, String email, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().api_bs_deviceFault(defaultPms(), faultCode, la, lo, boxCode, content, email);
        query(observable, callBack);
    }

    public void api_bs_businCoo(int type, String contactEmail, String businessName, String contactName, String contactPhone, String remark, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().api_bs_businCoo(defaultPms(), type, contactEmail, businessName, contactName, contactPhone, remark);
        query(observable, callBack);
    }

    public void recharge_getRechargeTable(String rcId, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().recharge_getRechargeTable(defaultPms(), rcId);
        query(observable, callBack);
    }

    public void recharge_listCard(NormalHttpCallBack<ResponseEntity<ArrayList<RechargeEntity>>> callBack) {
        Observable<ResponseEntity<ArrayList<RechargeEntity>>> observable = getInstance().getService().recharge_listCard(defaultPms(), defaultPms());
        query(observable, callBack);
    }


    public void editPwd(String areaCode, String username, String code, String password, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().editPwd(defaultPms(), areaCode, username, code, password);
        query(observable, callBack);
    }

    public void order_purchase(String orderId, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().order_purchase(defaultPms(), orderId);
        query(observable, callBack);
    }

    public void order_delOrder(String orderId, NormalHttpCallBack<ResponseEntity> callBack) {
        Observable<ResponseEntity> observable = getInstance().getService().order_delOrder(defaultPms(), orderId);
        query(observable, callBack);
    }

    public void advert_find(NormalHttpCallBack<ResponseEntity<ADDataEntity>> callBack) {
        Observable<ResponseEntity<ADDataEntity>> observable = getInstance().getService().advert_find(defaultPms(), defaultPms());
        query(observable, callBack);
    }

}
