package com.batterbox.power.phone.app.act;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.MainBannerImageLoader;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.dialog.ShopDesDialog;
import com.batterbox.power.phone.app.entity.ADDataEntity;
import com.batterbox.power.phone.app.entity.ADEntity;
import com.batterbox.power.phone.app.entity.DeviceEntity;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.LocMap;
import com.batterbox.power.phone.app.entity.OrderEntity;
import com.batterbox.power.phone.app.entity.ShopDetailEntity;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CountDownHelper;
import com.batterbox.power.phone.app.utils.GoogleMapHelper;
import com.batterbox.power.phone.app.utils.LocMapUtil;
import com.batterbox.power.phone.app.utils.RefreshLanguageHelper;
import com.batterbox.power.phone.app.utils.ScanBarCodeHelper;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.MathsUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.CircleImageView;
import com.chenyi.baselib.widget.PhotoPickHelper;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by ass on 2019-07-29.
 * Description
 */
@RuntimePermissions
public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    MapView mapView;
    GoogleMap googleMap;
    ImageView selectLocationIv;
//    MainMenuHelper mainMenuHelper;
    ArrayList<LBShopEntity> showLBShops = new ArrayList<>();
    Disposable skillDisposable;
    TextView deviceUseTimeTv;
    LatLng pt;
    FusedLocationProviderClient fusedLocationProviderClient;

    ADDataEntity adDataEntity;
    Banner aDBanner;

    public void initCountDown(long time) {
        long t = 60 * 60 * 24 * 10;//10??? ???

        skillDisposable = CountDownHelper.buildCountDown(t, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                FQL.d("?????????", "??????");
            }

            @Override
            public void onNext(Long aLong) {
                long countTime = t - aLong + time;
//                FQL.d("?????????", "countTime=" + countTime);
//                deviceUseTimeTv.setText(getString(R.string.main_11, OrderHelper.getShowTime(countTime)));
                deviceUseTimeTv.setText(getString(R.string.main_11, String.valueOf(countTime / 60)));
            }

            @Override
            public void onError(Throwable e) {
                FQL.d("?????????", "??????");
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void cancelCountDown() {
        if (skillDisposable != null) CountDownHelper.cancelCountDown(skillDisposable);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RefreshLanguageHelper.init(this);
        setContentView(R.layout.act_main);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffb331"));
//        mainMenuHelper = new MainMenuHelper(this);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.act_main_menu_btn).setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        mapView = findViewById(R.id.mapview);
        selectLocationIv = findViewById(R.id.act_main_select_location_iv);
        deviceUseTimeTv = findViewById(R.id.act_main_borrow_cur_time_tv);
        findViewById(R.id.act_login_submit_ll).setOnClickListener(v -> {
//            ARouteHelper.scan().navigation(this, 123);

            ScanBarCodeHelper.scan(MainActivity.this, result -> {
                FQL.e("onScanSuccess: " + result);
                String scanUrl = result.getContent();
                if (!StringUtil.isEmpty(scanUrl)) {
                    getDeviceInfo(scanUrl);
                }
            });
        });
        findViewById(R.id.act_main_shop_search_tv).setOnClickListener(v -> {
            if (!StringUtil.isEmpty(showLBShops)) {
                if (pt != null) {
                    for (LBShopEntity lbShopEntity : showLBShops) {
                        double dis = GoogleMapHelper.getDistance(pt.latitude, pt.longitude, lbShopEntity.la, lbShopEntity.lo);
                        lbShopEntity.dis = dis;
                    }
                    Collections.sort(showLBShops, (o1, o2) -> (int) (o1.dis * 1000) - (int) (o2.dis * 1000));
                }
                ARouteHelper.shop_list(showLBShops).navigation();
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int errorCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS != errorCode) {
            GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this, 0).show();
        } else {
            mapView.getMapAsync(this);
        }
        findViewById(R.id.act_main_refresh_iv).setOnClickListener(v -> {
//            startSelectLocationAnim();??
//            getLB(pt);
            startActivity(new Intent(this,com.batterbox.power.phone.app.act.main.MainActivity.class));
        });
        findViewById(R.id.act_main_help_iv).setOnClickListener(v -> ARouteHelper.helper_detail(pt == null ? "" : String.valueOf(pt.latitude), pt == null ? "" : String.valueOf(pt.longitude)).navigation());
        findViewById(R.id.act_main_nav_iv).setOnClickListener(v -> locCur());
        initADBanner();
//        VersionHelper.checkVersion(this);
        getADData();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        mainMenuHelper.setData();
        if (!UserUtil.isLogin()) {
            UserUtil.gotoLogin();
            return;
        }
        HttpClient.getInstance().user_info(new NormalHttpCallBack<ResponseEntity<UserEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<UserEntity> responseEntity) {
                if (responseEntity != null) {
                    UserUtil.saveUserInfo(responseEntity.getData());
//                    mainMenuHelper.setData();
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {

            }
        });
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ImageLoaderUtil.load(this, userEntity.headImg, findViewById(R.id.act_main_menu_btn), R.mipmap.ic_launcher);
        } else {
            ((CircleImageView) findViewById(R.id.act_main_menu_btn)).setImageResource(R.mipmap.ic_launcher);
        }
        order_UnfinishOrder();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap == null) return;
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(MainActivity.this);
        MainActivityPermissionsDispatcher.userLocationWithPermissionCheck(this);
    }

    private void locCur() {
        if (fusedLocationProviderClient == null) return;
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                if (location != null) {
//                    BatterBoxApp.lat = location.getLatitude();
//                    BatterBoxApp.lng = location.getLongitude();
//                    FQT.showShort(MainActivity.this, "location.getLongitude()=" + location.getLatitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
                }
            }
        });
    }

    private void startSelectLocationAnim() {
        TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, 20, -20);
        translateAnimation1.setDuration(220);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, -20, 20);
        translateAnimation2.setStartOffset(220);
        translateAnimation2.setDuration(220);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimation1);
        animationSet.addAnimation(translateAnimation2);
        selectLocationIv.startAnimation(animationSet);
    }

    ArrayList<Marker> markers = new ArrayList<>();

    private void getLB(LatLng latLng) {
        if (latLng == null) return;
        BatterBoxApp.lat = latLng.latitude;
        BatterBoxApp.lng = latLng.longitude;
        HttpClient.getInstance().bs(latLng.latitude, latLng.longitude, new NormalHttpCallBack<ResponseEntity<ArrayList<LBShopEntity>>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<LBShopEntity>> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    for (LBShopEntity lbShopEntity : responseEntity.getData()) {
//                        if (!showLBShops.contains(lbShopEntity)) {
//                            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lbShopEntity.la, lbShopEntity.lo)).icon(BitmapDescriptorFactory.fromResource(lbShopEntity.getIconRes())));
//                            marker.setTag(lbShopEntity);
//                            markers.add(marker);
//                            showLBShops.add(lbShopEntity);
//                        } else {
//                            for (Marker marker : markers) {
//                                if (marker.getTag() != null && marker.getTag() instanceof LBShopEntity && marker.getTag().equals(lbShopEntity)) {
//                                    marker.setTag(lbShopEntity);
//                                }
//                            }
//                        }
                        if (!showLBShops.contains(lbShopEntity)) {
                            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lbShopEntity.la, lbShopEntity.lo)).icon(BitmapDescriptorFactory.fromResource(lbShopEntity.getIconRes())));
                            marker.setTag(lbShopEntity);
                            showLBShops.add(lbShopEntity);
                        }
                    }
                }
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<LBShopEntity>> responseEntity, String msg) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountDown();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
//            String scanUrl = data.getStringExtra("scan_url");
//            if (!StringUtil.isEmpty(scanUrl)) {
//                getDeviceInfo(scanUrl);
//            }
//        }
//    }

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
                FQT.showShort(MainActivity.this, msg);
            }
        });
    }

    private void order_UnfinishOrder() {
        cancelCountDown();
        HttpClient.getInstance().order_UnfinishOrder(new NormalHttpCallBack<ResponseEntity<OrderEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<OrderEntity> responseEntity) {
                findViewById(R.id.act_main_borrow_cur_rl).setVisibility(View.GONE);
                if (responseEntity != null && responseEntity.getData() != null) {
                    if (responseEntity.getData().state == 1) {
                        //?????????
                        findViewById(R.id.act_main_borrow_cur_rl).setVisibility(View.VISIBLE);
                        findViewById(R.id.act_main_borrow_cur_rl).setOnClickListener(v -> ARouteHelper.order_detail(responseEntity.getData()).navigation());
                        long time = (long) responseEntity.getData().sysTime;
                        if (time > 0) {
//                            cancelCountDown();
                            initCountDown(time);
                        }
                    }
                }
            }

            @Override
            public void onFail(ResponseEntity<OrderEntity> responseEntity, String msg) {
                FQT.showShort(MainActivity.this, msg);
                findViewById(R.id.act_main_borrow_cur_rl).setVisibility(View.GONE);
            }
        });
    }

    long temp = 0L;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exit() {
        if (System.currentTimeMillis() - temp < 2000) {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            FQT.showShort(getApplicationContext(), getString(R.string.app_1));
        }
        temp = System.currentTimeMillis();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() instanceof LBShopEntity) {
            LBShopEntity lbShopEntity = (LBShopEntity) marker.getTag();
            HttpClient.getInstance().bs_findShopDetail(lbShopEntity.shopId, new NormalHttpCallBack<ResponseEntity<ShopDetailEntity>>(this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<ShopDetailEntity> responseEntity) {
                    if (responseEntity != null && responseEntity.getData() != null) {
                        lbShopEntity.rentCount = responseEntity.getData().rentCount;
                        lbShopEntity.returnCount = responseEntity.getData().returnCount;
                    }
                    showDesDialog(lbShopEntity);
                }

                @Override
                public void onFail(ResponseEntity<ShopDetailEntity> responseEntity, String msg) {
                    showDesDialog(lbShopEntity);
                }
            });


        }
        return true;
    }

    private void showDesDialog(LBShopEntity lbShopEntity) {
        ShopDesDialog shopDesDialog = new ShopDesDialog();
        shopDesDialog.setLbShopEntity(lbShopEntity);
        shopDesDialog.setOnNavListener(lbShopEntity1 -> {
            List<LocMap> locMaps = LocMapUtil.getLocMapList(MainActivity.this);
            if (locMaps.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (LocMap locMap : locMaps) {
                    list.add(locMap.appName);
                }
                DialogUtils.showListDialog(MainActivity.this, null, list, position -> {
                    LocMap locMap = locMaps.get(position);
                    LocMapUtil.navigationByLocMap(MainActivity.this, MathsUtil.round(lbShopEntity1.la, 6), MathsUtil.round(lbShopEntity1.lo, 6), StringUtil.fixNullStr(lbShopEntity1.shopName, "location"), StringUtil.fixNullStr(lbShopEntity1.shopAdress), locMap.packageName);
                });
            } else {
                FQT.showShort(MainActivity.this, "no map app client");
            }
        });
        shopDesDialog.show(getSupportFragmentManager(), ShopDesDialog.class.getName());
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void userLocation() {
        googleMap.setMyLocationEnabled(true);
//        FQT.showShort(this, "onMapReady");
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(13.0f));
        //TODO ??????  ??????????????? latitude=36.67384405362361&longitude=-4.4784364476799965&
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.6901535, -4.4734894), 13.0f));
        //TODO ?????? ????????????
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.220299, 113.290519), 13.0f));

        googleMap.setOnCameraIdleListener(() -> {
            int left = mapView.getLeft();
            int top = mapView.getTop();
            int right = mapView.getRight();
            int bottom = mapView.getBottom();
            int x = (int) (mapView.getX() + (right - left) / 2);
            int y = (int) (mapView.getY() + (bottom - top) / 2);
            Projection projection = googleMap.getProjection();
            pt = projection.fromScreenLocation(new Point(x, y));
//            FQT.showShort(MainActivity.this, "location.getLongitude()=" + pt.toString());
            startSelectLocationAnim();
            getLB(pt);
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locCur();
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location location = locationResult.getLastLocation();
//                FQL.d("locloc", "onLocationResult");
//                if (location != null) {
//                    FQL.d("locloc", "location===" + location.toString());
//                    FQT.showShort(MainActivity.this, "location.getLongitude()=" + location.getLatitude());
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
//                    fusedLocationProviderClient.removeLocationUpdates(this);
//                }
//            }
//
//            @Override
//            public void onLocationAvailability(LocationAvailability locationAvailability) {
//                super.onLocationAvailability(locationAvailability);
//                FQL.d("locloc", "locationAvailability.isLocationAvailable()==" + locationAvailability.isLocationAvailable());
//            }
//        }, null);

    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showNeverAskForCamera() {
        PhotoPickHelper.showDefindDialog(this, getSupportFragmentManager());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public static void goToMain(Context context, boolean newTask) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void initADBanner() {
        aDBanner = findViewById(R.id.act_main_shop_ad_banner);
        aDBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        aDBanner.setImageLoader(new MainBannerImageLoader());
        aDBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        aDBanner.setOnBannerListener(position -> {
            if (adDataEntity != null && !StringUtil.isEmpty(adDataEntity.adverts) && position < adDataEntity.adverts.size()) {
                ADEntity adEntity = adDataEntity.adverts.get(position);
                if (adEntity.type != null) {
                    String firstParam = null;
                    String secondParam = null;
                    if (!StringUtil.isEmpty(adEntity.typeValue)) {
                        if (adEntity.typeValue.length > 0 && !StringUtil.isEmpty(adEntity.typeValue[0]))
                            firstParam = adEntity.typeValue[0];
                        if (adEntity.typeValue.length > 1 && !StringUtil.isEmpty(adEntity.typeValue[1]))
                            secondParam = adEntity.typeValue[1];
                    }
                    switch (adEntity.type) {
                        case "Web":
                            if (firstParam != null) {
                                ARouteHelper.hybrid_nav(firstParam).navigation();
                            }
                            break;
                        case "AppPage":
                            if (firstParam != null) {
                                switch (firstParam) {
                                    case "Recharge"://??????????????????)
                                        ARouteHelper.recharge_list().navigation();
                                        break;
                                    case "popu"://???????????????????????????
                                        ARouteHelper.share().navigation();
                                        break;
                                    case "shop"://???????????????????????????????????????????????????id
                                        if (!StringUtil.isEmpty(secondParam)) {
                                            LBShopEntity lbShopEntity = new LBShopEntity();
                                            lbShopEntity.shopId = secondParam;
                                            ARouteHelper.shop_detail(lbShopEntity).navigation();
                                        }
                                        break;
                                }
                            }
                            break;
                    }
                }

            }
        });
    }

    private void getADData() {
        HttpClient.getInstance().advert_find(new NormalHttpCallBack<ResponseEntity<ADDataEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ADDataEntity> responseEntity) {
                adDataEntity = responseEntity.getData();
                if (adDataEntity != null && !StringUtil.isEmpty(adDataEntity.adverts)) {
                    aDBanner.setVisibility(View.VISIBLE);
                    if (adDataEntity.times > 0) {
                        aDBanner.setDelayTime(adDataEntity.times * 1000);
                    }
                    aDBanner.update(adDataEntity.adverts);
                } else {
                    aDBanner.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(ResponseEntity<ADDataEntity> responseEntity, String msg) {
                aDBanner.setVisibility(View.GONE);
            }
        });
    }
}