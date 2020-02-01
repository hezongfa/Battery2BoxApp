package com.batterbox.power.phone.app.act;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.GlideImageLoader;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.dialog.ShopCouponDialog;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.LocMap;
import com.batterbox.power.phone.app.entity.ShopDetailEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.LocMapUtil;
import com.batterbox.power.phone.app.utils.ScanBarCodeHelper;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.MathsUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.utils.sys.SysUtil;
import com.chenyi.baselib.widget.PhotoPickHelper;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-08-05.
 * Description
 */
@RuntimePermissions
@Route(path = ARouteHelper.SHOP_DETAIL)
public class ShopDetailActivity extends NavigationActivity {
    Banner banner;
    TextView indexTv;
    @Autowired
    public LBShopEntity lbShopEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.act_shop_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationVisible(false);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"), 40);
        findViewById(R.id.act_shop_detail_close_iv).setOnClickListener(v -> finish());
        indexTv = findViewById(R.id.act_shop_detail_img_index_tv);
        banner = findViewById(R.id.act_shop_detail_img_banner);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int size = 0;
                if (indexTv.getTag() != null && indexTv.getTag() instanceof Integer) {
                    size = (int) indexTv.getTag();
                }
                indexTv.setText(position + 1 + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (lbShopEntity != null) {
            initData(lbShopEntity.shopName, lbShopEntity.shopAdress, lbShopEntity.rentCount, lbShopEntity.returnCount, lbShopEntity.la, lbShopEntity.lo);
            HttpClient.getInstance().bs_findShopDetail(lbShopEntity.shopId, new NormalHttpCallBack<ResponseEntity<ShopDetailEntity>>(this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<ShopDetailEntity> responseEntity) {
                    if (responseEntity != null && responseEntity.getData() != null) {
                        initData(responseEntity.getData().shopName, responseEntity.getData().shopAdress, responseEntity.getData().rentCount, responseEntity.getData().returnCount, responseEntity.getData().la, responseEntity.getData().lo);
                        findViewById(R.id.act_shop_detail_coupon_iv).setVisibility(responseEntity.getData().couponCon > 0 ? View.VISIBLE : View.GONE);
                        findViewById(R.id.act_shop_detail_coupon_iv).setOnClickListener(v -> {
                            ShopCouponDialog shopCouponDialog = new ShopCouponDialog();
                            shopCouponDialog.setShopEntity(responseEntity.getData());
                            shopCouponDialog.show(getSupportFragmentManager());
                        });
                        if (responseEntity.getData().img != null) {
                            banner.setOnBannerListener(position -> {
                                ARouteHelper.photo_list(responseEntity.getData().img).navigation();
                            });
                            banner.update(responseEntity.getData().img);
                            indexTv.setTag(responseEntity.getData().img.size());
                            if (!StringUtil.isEmpty(responseEntity.getData().img)) {
                                indexTv.setText("1/" + responseEntity.getData().img.size());
                            }
                        }
                        findViewById(R.id.act_shop_detail_wifi_iv).setVisibility(responseEntity.getData().wifi == 1 ? View.VISIBLE : View.GONE);
                        findViewById(R.id.act_shop_detail_smoke_iv).setVisibility(responseEntity.getData().smoke == 1 ? View.VISIBLE : View.GONE);
                        ((TextView) findViewById(R.id.act_shop_detail_web_tv)).setText(StringUtil.fixNullStr(responseEntity.getData().url));
                        if (!StringUtil.isEmpty(responseEntity.getData().url)) {
                            findViewById(R.id.act_shop_detail_web_tv).setOnClickListener(v -> {
                                if (StringUtil.isEmpty(responseEntity.getData().url)) return;
                                String url = responseEntity.getData().url;
                                if (!responseEntity.getData().url.startsWith("http")) {
                                    url = "http://" + responseEntity.getData().url;
                                }
                                SysUtil.openUrlSelectWeb(ShopDetailActivity.this, url);
                            });
                        }
                        ((TextView) findViewById(R.id.act_shop_detail_shape_tv)).setText(StringUtil.fixNullStr(responseEntity.getData().email));
                        ((TextView) findViewById(R.id.act_shop_detail_tel_tv)).setText(StringUtil.fixNullStr(responseEntity.getData().phone));
                        if (!StringUtil.isEmpty(responseEntity.getData().phone)) {
                            findViewById(R.id.act_shop_detail_tel_tv).setOnClickListener(v -> ShopDetailActivityPermissionsDispatcher.callPhoneWithPermissionCheck(ShopDetailActivity.this, responseEntity.getData().phone));
                        }
                        ((TextView) findViewById(R.id.act_shop_detail_time_tv)).setText(StringUtil.fixNullStr(responseEntity.getData().businessTime));
                        ((TextView) findViewById(R.id.act_shop_detail_address_tv)).setText(StringUtil.fixNullStr(responseEntity.getData().shopAdress));
                    }
                }

                @Override
                public void onFail(ResponseEntity<ShopDetailEntity> responseEntity, String msg) {
                    FQT.showShort(ShopDetailActivity.this, msg);
                }
            });
        }
    }

    private void initData(String shopName, String shopAdress, String rentCount, String returnCount, double la, double lo) {
        ((TextView) findViewById(R.id.act_shop_detail_title_tv)).setText(StringUtil.fixNullStr(shopName));
        ((TextView) findViewById(R.id.act_shop_detail_borrow_tv)).setText(StringUtil.fixNullStr(rentCount, "0"));
        ((TextView) findViewById(R.id.act_shop_detail_return_tv)).setText(StringUtil.fixNullStr(returnCount, "0"));
        findViewById(R.id.act_shop_detail_scan_ll).setOnClickListener(v -> ScanBarCodeHelper.scan(ShopDetailActivity.this, result -> {
            String scanUrl = result.getContent();
            if (!StringUtil.isEmpty(scanUrl)) {
                getDeviceInfo(scanUrl);
            }
        }));
        findViewById(R.id.act_shop_detail_nav_iv).setOnClickListener(v -> {
            List<LocMap> locMaps = LocMapUtil.getLocMapList(ShopDetailActivity.this);
            if (locMaps.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (LocMap locMap : locMaps) {
                    list.add(locMap.appName);
                }
                DialogUtils.showListDialog(ShopDetailActivity.this, null, list, position -> {
                    LocMap locMap = locMaps.get(position);
                    LocMapUtil.navigationByLocMap(ShopDetailActivity.this, MathsUtil.round(la, 6), MathsUtil.round(lo, 6), StringUtil.fixNullStr(shopName, "location"), StringUtil.fixNullStr(shopAdress), locMap.packageName);
                });
            } else {
                FQT.showShort(ShopDetailActivity.this, "no map app client");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            String scanUrl = data.getStringExtra("scan_url");
            if (!StringUtil.isEmpty(scanUrl)) {
                getDeviceInfo(scanUrl);
            }
        }
    }

    private void getDeviceInfo(String url) {
        HttpClient.getInstance().find_box(url, new NormalHttpCallBack<ResponseEntity<DeviceEntity>>(this) {
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
                FQT.showShort(ShopDetailActivity.this, msg);
            }
        });
    }


    @NeedsPermission({Manifest.permission.CALL_PHONE})
    public void callPhone(String phone) {
        SysUtil.callTel(ShopDetailActivity.this, phone);
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE})
    void showNeverAskForPermission() {
        PhotoPickHelper.showDefindDialog(this, getSupportFragmentManager());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShopDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
