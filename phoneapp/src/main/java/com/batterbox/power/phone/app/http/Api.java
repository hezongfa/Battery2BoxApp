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
import com.batterbox.power.phone.app.entity.SearchUserEntity;
import com.batterbox.power.phone.app.entity.SharePageEntity;
import com.batterbox.power.phone.app.entity.ShopDetailEntity;
import com.batterbox.power.phone.app.entity.TimeRecordEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.entity.VersionEntity;
import com.chenyi.baselib.entity.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mac on 04/01/2018.
 *
 * @Field 传递 array 参数名要 加[]  比如 ids[]
 */

public interface Api {
    @Multipart
    @POST("us/upLoadImgFile")
    Observable<ResponseEntity<String>> uploadSingleImg(@HeaderMap Map<String, Object> map, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("openApi/countryPrefix")
    Observable<AreaCodeDataEntity> openApi_countryPrefix(@HeaderMap Map<String, Object> map,
                                                         @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("openApi/findMemExistByPhoneAndArea")
    Observable<ResponseEntity> findMemExistByPhoneAndArea(@HeaderMap Map<String, Object> map,
                                                          @Field("areaCode") String areaCode,
                                                          @Field("phone") String username);

    @FormUrlEncoded
    @POST("openApi/byPwdReg")
    Observable<ResponseEntity> register(@HeaderMap Map<String, Object> map,
                                        @Field("areaCode") String areaCode,
                                        @Field("phone") String username,
                                        @Field("vCode") String vCode,
                                        @Field("pwd") String password,
                                        @Field("email") String email);

    @FormUrlEncoded
    @POST("openApi/pwdLogin")
    Observable<ResponseEntity<UserEntity>> login(@HeaderMap Map<String, Object> map,
                                                 @Field("areaCode") String areaCode,
                                                 @Field("phone") String username,
                                                 @Field("pwd") String password);

    @FormUrlEncoded
    @POST("openApi/codeIsCorrect")
    Observable<ResponseEntity> codeIsCorrect(@HeaderMap Map<String, Object> map,
                                             @Field("phone") String username,
                                             @Field("vCode") String vCode);

    @FormUrlEncoded
    @POST("us/userInfo")
    Observable<ResponseEntity<UserEntity>> user_info(@HeaderMap Map<String, Object> map, @FieldMap Map<String, Object> map1);


    @FormUrlEncoded
    @POST("openApi/sendSms")
    Observable<ResponseEntity> getVerCode(@HeaderMap Map<String, Object> map,
                                          @Field("areaCode") String areaCode,
                                          @Field("phone") String username);


    @FormUrlEncoded
    @POST("bs/roundBox")
    Observable<ResponseEntity<ArrayList<LBShopEntity>>> bs(@HeaderMap Map<String, Object> map,
                                                           @Field("latitude") double latitude,
                                                           @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("bs/findBoxCodeByUrl")
    Observable<ResponseEntity<DeviceEntity>> find_box(@HeaderMap Map<String, Object> map,
                                                      @Field("url") String url);

    @FormUrlEncoded
    @POST("bs/findCouponsByShop")
    Observable<ResponseEntity<ArrayList<CouponEntity>>> bs_findCouponsByShop(@HeaderMap Map<String, Object> map,
                                                                             @Field("shopId") String shopId);

    @FormUrlEncoded
    @POST("bs/newFindShopCoupons")
    Observable<ResponseEntity<ArrayList<LbsShopCouponEntity>>> bs_findShopCoupons(@HeaderMap Map<String, Object> map,
                                                                                  @FieldMap Map<String, Object> map1,
                                                                                  @Field("latitude") double latitude,
                                                                                  @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("order/borrow")
    Observable<ResponseEntity<BorrowResultEntity>> order_borrow(@HeaderMap Map<String, Object> map,
                                                                @Field("boxCode") String boxCode);

    @FormUrlEncoded
    @POST("order/queryOrder0State")
    Observable<ResponseEntity<OrderStateEntity>> order_state(@HeaderMap Map<String, Object> map,
                                                             @Field("orderCode") String orderCode);

    @FormUrlEncoded
    @POST("order/UnfinishOrder")
    Observable<ResponseEntity<OrderEntity>> order_UnfinishOrder(@HeaderMap Map<String, Object> map,
                                                                @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("order/findMyOrderList")
    Observable<ResponseEntity<ArrayList<OrderEntity>>> order_findMyOrderList(@HeaderMap Map<String, Object> map,
                                                                             @Field("pageNum") int pageNum,
                                                                             @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("us/freetimeDetail")
    Observable<ResponseEntity<ArrayList<TimeRecordEntity>>> us_freetimeDetail(@HeaderMap Map<String, Object> map,
                                                                              @Field("pageNum") int pageNum,
                                                                              @Field("pageSize") int pageSize);


    @FormUrlEncoded
    @POST("us/setUserInfo")
    Observable<ResponseEntity> us_setUserInfo(@HeaderMap Map<String, Object> map, @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("us/tieCard")
    Observable<ResponseEntity> us_tieCard(@HeaderMap Map<String, Object> map,
                                          @Field("cardNum") String cardNum,
                                          @Field("cardUserName") String cardUserName,
                                          @Field("cardExpirationData") String cardExpirationData,
                                          @Field("cvv") String cvv,
                                          @Field("cardType") String cardType);

    @FormUrlEncoded
    @POST("us/queryCard")
    Observable<ResponseEntity<CardEntity>> us_queryCard(@HeaderMap Map<String, Object> map,
                                                        @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("us/deleCard")
    Observable<ResponseEntity> us_deleCard(@HeaderMap Map<String, Object> map,
                                           @Field("mcId") String mcId);

    @FormUrlEncoded
    @POST("us/modifyPwd")
    Observable<ResponseEntity> us_modifyPwd(@HeaderMap Map<String, Object> map,
                                            @Field("oldPwd") String oldPwd,
                                            @Field("newPwd") String newPwd);

    @FormUrlEncoded
    @POST("us/billDetail")
    Observable<ResponseEntity<ArrayList<BillEntity>>> us_billDetail(@HeaderMap Map<String, Object> map,
                                                                    @Field("pageNum") int pageNum,
                                                                    @Field("pageSize") int pageSize);


    @FormUrlEncoded
    @POST("us/getNewVersion")
    Observable<ResponseEntity<VersionEntity>> us_getNewVersion(@HeaderMap Map<String, Object> map,
                                                               @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("us/myInvitation")
    Observable<ResponseEntity<SharePageEntity>> us_myInvitation(@HeaderMap Map<String, Object> map,
                                                                @FieldMap Map<String, Object> map1);


    @FormUrlEncoded
    @POST("us/updateJiguangId")
    Observable<ResponseEntity> us_updateJiguangId(@HeaderMap Map<String, Object> map,
                                                  @Field("registrationID") String registrationID);

    @FormUrlEncoded
    @POST("bs/findShopDetail")
    Observable<ResponseEntity<ShopDetailEntity>> bs_findShopDetail(@HeaderMap Map<String, Object> map,
                                                                   @Field("shopId") String shopId);

    @FormUrlEncoded
    @POST("api/bs/deviceFault")
    Observable<ResponseEntity> api_bs_deviceFault(@HeaderMap Map<String, Object> map,
                                                  @Field("faultCode") String faultCode,
                                                  @Field("la") String la,
                                                  @Field("lo") String lo,
                                                  @Field("boxCode") String boxCode,
                                                  @Field("content") String content,
                                                  @Field("email") String email);

    @FormUrlEncoded
    @POST("api/bs/businCoo")
    Observable<ResponseEntity> api_bs_businCoo(@HeaderMap Map<String, Object> map,
                                               @Field("type") int type,
                                               @Field("contactEmail") String contactEmail,
                                               @Field("businessName") String businessName,
                                               @Field("contactName") String contactName,
                                               @Field("contactPhone") String contactPhone,
                                               @Field("remark") String remark);

    @FormUrlEncoded
    @POST("recharge/getRecharge")
    Observable<ResponseEntity> recharge_getRechargeTable(@HeaderMap Map<String, Object> map,
                                                         @Field("rcId") String rcId);


    @FormUrlEncoded
    @POST("recharge/listCard")
    Observable<ResponseEntity<ArrayList<RechargeEntity>>> recharge_listCard(@HeaderMap Map<String, Object> map,
                                                                            @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("openApi/editPwd")
    Observable<ResponseEntity> editPwd(@HeaderMap Map<String, Object> map,
                                       @Field("areaCode") String areaCode,
                                       @Field("phone") String username,
                                       @Field("vCode") String vCode,
                                       @Field("pwd") String password);

    @FormUrlEncoded
    @POST("order/purchase")
    Observable<ResponseEntity> order_purchase(@HeaderMap Map<String, Object> map,
                                              @Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("order/delOrder")
    Observable<ResponseEntity> order_delOrder(@HeaderMap Map<String, Object> map,
                                              @Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("advert/find")
    Observable<ResponseEntity<ADDataEntity>> advert_find(@HeaderMap Map<String, Object> map,
                                                         @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("cp/myCoupons")
    Observable<ResponseEntity<ArrayList<CouponEntity>>> cp_myCoupons(@HeaderMap Map<String, Object> map,
                                                                     @Field("type") int type,
                                                                     @Field("pageNum") int pageNum,
                                                                     @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("cp/findCouponByCode")
    Observable<ResponseEntity<CouponEntity>> cp_findCouponByCode(@HeaderMap Map<String, Object> map,
                                                                 @Field("code") String code);

    @FormUrlEncoded
    @POST("cp/useMyCoupon")
    Observable<ResponseEntity> cp_useMyCoupon(@HeaderMap Map<String, Object> map,
                                              @Field("id") long id);

    @FormUrlEncoded
    @POST("cp/deleMyCoupon")
    Observable<ResponseEntity> cp_deleMyCoupon(@HeaderMap Map<String, Object> map,
                                               @Field("id") long id);

    @FormUrlEncoded
    @POST("im/getSign")
    Observable<ResponseEntity<String>> im_getSign(@HeaderMap Map<String, Object> map,
                                                  @FieldMap Map<String, Object> map1);

    @FormUrlEncoded
    @POST("im/searchMember")
    Observable<ResponseEntity<SearchUserEntity>> im_searchMember(@HeaderMap Map<String, Object> map,
                                                                 @Field("data") String data);


}
